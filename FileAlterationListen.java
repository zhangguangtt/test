import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FileAlterationListen extends FileAlterationListenerAdaptor {

    public static String Reportname = "gsqxjb_risk_zyt_road";

    public static String FileLocaltion = File.separator+"static"+File.separator+"pdf"+File.separator;

    public File DirContext;

    public String new_path = null;

    public FileAlterationListen(File dirContext) {
        super();
        DirContext = dirContext;
    }

    //文件夹创建
    @Override
    public void onDirectoryCreate(File directory) {
        System.out.println(directory.getName()+"  |  文件夹被创建"+"  |  路径为："+directory.getPath());
    }
    //文件夹改变
    @Override
    public void onDirectoryChange(File directory) {
        System.out.println(directory.getName()+"  |  文件夹被改变"+"  |   路径为："+directory.getPath());
    }

    //文件夹删除
    @Override
    public void onDirectoryDelete(File directory) {
        System.out.println(directory.getName()+"  |  文件夹被删除"+"  |   路径为："+directory.getPath());
        System.out.println("cancel");
    }

    //文件创建
    @Override
    public void onFileCreate(File file) {
        super.onFileCreate(file);
        System.out.println(file.getName() +"  |  文件被创建"+"  |   路径为："+file.getPath());
        String file_name = file.getName();
        String[] encode_substr = file_name.substring(0, file_name.lastIndexOf('.')).split("_");
        if (encode_substr.length == 3){
            new_path = file.getPath().replace("pictures\\" + file.getName(),"").concat("1\\" + encode_substr[0]).concat("\\" + encode_substr[1].substring(0,1)).concat("\\" + encode_substr[1].substring(1,2)).concat("\\" + encode_substr[1]);
        }
        else if (encode_substr.length == 5){
            Date date=new Date();//此时date为当前的时间
            SimpleDateFormat dateFormat=new SimpleDateFormat("YYYYMMdd");//设置当前时间的格式，为年月日

            if (encode_substr[2].equals("-2")){
                new_path = file.getPath().replace("pictures\\" + file.getName(),"").concat("0\\" + encode_substr[0]).concat("\\3" ).concat("\\" + dateFormat.format(date));
            }
            else if (encode_substr[3].equals("0.7")){
                if (Integer.parseInt(encode_substr[1]) > 0){
                    new_path = file.getPath().replace("pictures\\" + file.getName(),"").concat("0\\" + encode_substr[0] + "\\2\\1\\").concat(encode_substr[2].substring(0,1)).concat("\\" + encode_substr[2].substring(1,2)).concat("\\" + encode_substr[2]);
                }else{
                    new_path = file.getPath().replace("pictures\\" + file.getName(),"").concat("0\\" + encode_substr[0] + "\\2\\-1\\").concat(encode_substr[2].substring(0,1)).concat("\\" + encode_substr[2].substring(1,2)).concat("\\" + encode_substr[2]);
                }
            }
            else if (encode_substr[3].equals("0.97")){
                if (Integer.parseInt(encode_substr[1]) > 0){
                    new_path = file.getPath().replace("pictures\\" + file.getName(),"").concat("0\\" + encode_substr[0] + "\\1\\1\\").concat(encode_substr[2].substring(0,1)).concat("\\" + encode_substr[2].substring(1,2)).concat("\\" + encode_substr[2]);
                }else{
                    new_path = file.getPath().replace("pictures\\" + file.getName(),"").concat("0\\" + encode_substr[0] + "\\1\\-1\\").concat(encode_substr[2].substring(0,1)).concat("\\" + encode_substr[2].substring(1,2)).concat("\\" + encode_substr[2]);
                }

            }
            else if (encode_substr[1].equals("-1") && encode_substr[2].equals("-1") && encode_substr[3].equals("-2")){
                new_path = file.getPath().replace("pictures\\" + file.getName(),"").concat("0\\" + encode_substr[0]).concat("\\2\\-1" ).concat("\\" + dateFormat.format(date));
            }
            else if (encode_substr[1].equals("-1") && encode_substr[2].equals("-1") && encode_substr[3].equals("-1")){
                new_path = file.getPath().replace("pictures\\" + file.getName(),"").concat("0\\" + encode_substr[0]).concat("\\2\\2" ).concat("\\" + dateFormat.format(date));
            }
            else if (encode_substr[1].equals("1") && encode_substr[2].equals("-1") && encode_substr[3].equals("-1")){
                new_path = file.getPath().replace("pictures\\" + file.getName(),"").concat("0\\" + encode_substr[0]).concat("\\2\\2" ).concat("\\" + dateFormat.format(date));
            }
            else if (encode_substr[1].equals("0") && encode_substr[2].equals("-1") && encode_substr[3].equals("-1")){
                new_path = file.getPath().replace("pictures\\" + file.getName(),"").concat("0\\" + encode_substr[0]).concat("\\2\\0" ).concat("\\" + dateFormat.format(date));
            }

        }

        File folder = new File(new_path);
        if (!folder.exists() && !folder.isDirectory()) {
            try {
                folder.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            System.out.println("创建文件夹");
        }
//        else {
//            System.out.println("文件夹已存在");
//        }
        File old_file = new File(file.getPath());
        try {
            old_file.renameTo(new File(new_path.concat("\\" + file.getName())));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println("File is moved successful!");
    }

    //文件夹改变
    @Override
    public void onFileChange(File file) {
        super.onFileChange(file);
        System.out.println(file.getName() +"   |   文件被修改"+"  |   路径为："+file.getPath());
        traverseFolder2(DirContext.getPath(),file.getName());
    }

    //文件删除
    @Override
    public void onFileDelete(File file) {
        super.onFileDelete(file);
        System.out.println(file.getName()+" 文件被删除"+"    路径为："+file.getPath());
    }

    public  void traverseFolder2(String path,String fileName) {

        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                return;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        traverseFolder2(file2.getAbsolutePath(),fileName);
                    } else {
                        if (fileName.equals(file2.getName())&&file2.getName().contains(Reportname)&&file2.getName().contains(".doc")) {
                            String name = file2.getName().substring(0, file2.getName().length()-4);
                            // 得到静态资源的相对地址
                            String Apath = FileAlterationListen.class.getClassLoader().getResource("").getPath().split("WEB-INF")[0].replaceAll("/", "\\\\"),subPath=Apath.substring(1, Apath.length());
                            File outFile = new File(subPath+FileLocaltion+name+".pdf");
                        }
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
    }

    public static void main(String[] args) {
        File dir = new File("C:\\Users\\Administrator\\Desktop\\测试\\pictures");
        FileAlterationMonitor monitor = new FileAlterationMonitor();

        IOFileFilter filter = FileFilterUtils.or(FileFilterUtils.directoryFileFilter(), FileFilterUtils.fileFileFilter());

        FileAlterationObserver observer = new FileAlterationObserver(dir,filter);
        observer.addListener(new FileAlterationListen(dir));

        monitor.addObserver(observer);
        try {
            //开始监听
            monitor.start();
            System.out.println("监听……");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
