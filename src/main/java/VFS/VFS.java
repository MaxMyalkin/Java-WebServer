package VFS;

import java.io.*;
import java.util.Iterator;

/*
 * Created by maxim on 12.04.14.
 */

public class VFS implements VirtualFileSystem {

    private String root;

    public VFS(String root){
        this.root = root;
    }

    @Override
    public String getAbsolutePath(String path) {
        return new File(root + path).getAbsolutePath();
    }

    @Override
    public boolean isExist(String path) {
        return new File(root + path).exists();
    }

    @Override
    public boolean isDirectory(String path) {
        return new File(root + path).isDirectory();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public byte[] getBytes(String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(root + path);
        byte[] array = new byte[fileInputStream.available()];
        fileInputStream.read(array);
        fileInputStream.close();
        return array;
    }

    @Override
    public String getUTF8Text(String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(root + path); //байты
        DataInputStream dataInputStream = new DataInputStream(fileInputStream); //примитивные типы
        InputStreamReader inputStreamReader = new InputStreamReader(dataInputStream); //строки
        BufferedReader bufferedInputStream = new BufferedReader(inputStreamReader); //эффективное считывание по строкам/массивам
        String utf8text = "";
        String str;

        while ((str = bufferedInputStream.readLine()) != null) {
            utf8text += str + '\n';
        }
        bufferedInputStream.close();
        return utf8text;
    }

    @Override
    public void iterateAll(String startDir) {
        FileNameIterator fileNameIterator = new FileNameIterator(root + startDir);
        while(fileNameIterator.hasNext()) {
            System.out.println(fileNameIterator.next());
        }
    }

    @Override
    public Iterator<String> getIterator(String startDir) {
        return new FileNameIterator(root + startDir);
    }


}
