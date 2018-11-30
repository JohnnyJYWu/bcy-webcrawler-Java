package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;

import model.Album;
import model.Artist;

/**
 * @version V1.0
 * @author JohnnyJYWu
 * @description 文件工具类，读写信息文件
 */

public class FileUtils {
	
	public static void writeStringToFile(String content, String filePath) {
        try {
            File file = new File(filePath);
            FileOutputStream out = new FileOutputStream(file);
            PrintStream ps = new PrintStream(out);
            ps.println(content);
            ps.flush();
            ps.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

	public static void writeArtistToFile(Artist obj, String filePath) {
        File file = new File(filePath);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
            objOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public static Artist readArtistFromFile(String filePath) {
		Artist temp = null;
        File file = new File(filePath);
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            ObjectInputStream objIn = new ObjectInputStream(in);
            temp = (Artist) objIn.readObject();
            objIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return temp;
    }
	
	public static void writeAlbumsToFile(Album obj, String filePath) {
        File file = new File(filePath);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
            objOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public static Album readAlbumsFromFile(String filePath) {
		Album temp = null;
        File file = new File(filePath);
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            ObjectInputStream objIn = new ObjectInputStream(in);
            temp = (Album) objIn.readObject();
            objIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return temp;
    }
}
