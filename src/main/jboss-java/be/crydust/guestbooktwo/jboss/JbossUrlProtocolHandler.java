package be.crydust.guestbook.jboss;

import java.io.IOException;
import java.net.URISyntaxException;
import static org.webjars.WebJarAssetLocator.WEBJARS_PATH_PREFIX;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;
import org.jboss.vfs.VirtualFileFilter;
import org.webjars.urlprotocols.UrlProtocolHandler;

public class JbossUrlProtocolHandler implements UrlProtocolHandler {

    @Override
    public boolean accepts(String protocol) {
        return "vfs".equals(protocol);
    }

    @Override
    public Set<String> getAssetPaths(URL url, final Pattern filterExpr, ClassLoader... classLoaders) {
        Set<String> assetPaths = new HashSet<>();
        try {
            final VirtualFile virtualFile = VFS.getChild(url.toURI());
            List<VirtualFile> children = virtualFile.getChildrenRecursively(new VirtualFileFilter() {
                @Override
                public boolean accepts(VirtualFile file) {
                    if (file.isDirectory()) {
                        return false;
                    }
                    int prefixIndex = file.getPathName().indexOf(WEBJARS_PATH_PREFIX);
                    if (prefixIndex == -1) {
                        return false;
                    }

                    final String relativePath = file.getPathName().substring(prefixIndex);

                    return file.isFile() && filterExpr.matcher(relativePath).matches();
                }
            });
            for (VirtualFile child : children) {
                assetPaths.add(child.getPathName().substring(child.getPathName().indexOf(WEBJARS_PATH_PREFIX)));
            }
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

        return assetPaths;
    }
}
