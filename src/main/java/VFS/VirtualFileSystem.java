package VFS;

/*
 * Created by maxim on 12.04.14.
 */

import java.io.IOException;
import java.util.Iterator;

@SuppressWarnings("UnusedDeclaration")
public interface VirtualFileSystem {

    String getAbsolutePath(String file);

    boolean isExist(String path);

    boolean isDirectory(String path);

    byte[] getBytes(String file) throws IOException;

    String getUTF8Text(String file) throws IOException;

    void iterateAll(String startDir);

    Iterator<String> getIterator(String startDir);
}
