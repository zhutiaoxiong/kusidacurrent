package common;

/**
 * Created by Administrator on 2016/9/13.
 */
public class SkinCheck {
//    public static boolean skin0IsInDisk= false , skin0IsUnZipping = false;
//    public static int skin0checkNum = 0;
//    private static String SkinDir;
//    private static String SkinBgDir;
//    public static String getSkinDir(){
//        if(SkinDir ==null)SkinDir = GlobalContext.getContext().getExternalFilesDir(null).getAbsolutePath()+ "/dress";
//        return SkinDir;
//    }
//    public static String getSkinBgDir(){
//        if(SkinBgDir ==null)SkinBgDir = GlobalContext.getContext().getExternalFilesDir(null).getAbsolutePath()+ "/carbg";
//        return SkinBgDir;
//    }
//    /**皮肤是否已解压*/
//    public static boolean skinIsInDisk(final int skinId) {
//        if(skinId == 0 && skin0IsInDisk ){
//            skin0checkNum++;
//            if(skin0checkNum%30!=0) {
//                return true;
//            }
//        }
////        File file = Environment.getExternalStorageDirectory();
////        file = new File(file, "/MessageFiles");
//        File file = new File(getSkinDir());
//        file.mkdirs();
//        File zipFile = new File(file, skinId + ".zip");
//        File setupFile = new File(file+"/"+skinId, "setup.txt");
////        File onePng = new File(file+"/"+skinId, "body"+.oipc);
//        if(skinId == 0){//默认皮肤的压缩包在安装文件里
//            if (setupFile.exists()){
//                skin0IsInDisk = true;
//                Log.w("SkinCheck","check once");
//                return true;
//            }
//        }else{
//            if (zipFile.exists() && setupFile.exists()) return true;
//        }
//        return false;
//    }
//
//    /**
//     * 检查目录中有没有这个文件
//     * @param skinId
//     * @return
//     */
//    public static boolean skinBgIsInDisk(final int skinId) {
//        File file=new File(getSkinBgDir());
//        File[] subFile = file.listFiles();
//        if(subFile==null||subFile.length==0)return false;
//        for(int i=0;i<subFile.length;i++){
////             if (BuildConfig.DEBUG) Log.e("观看皮肤", "subFile[i].getName()== "+subFile[i].getName()+"本车装扮id"+skinId);
//            if(subFile[i].getName().equals(""+skinId)){
//                return true;
//            }
//        }
//        return false;
//    }
//    public static boolean isNewSkinAddBG() {
//        File file=new File(GlobalContext.getContext().getExternalFilesDir(null).getAbsolutePath()+"/"+"newversion");
//        if(file.exists()){
//            return  true;
//        }
//        return false;
//    }
//    public static void createNewSkinAddBG(){
//        File file=new File(GlobalContext.getContext().getExternalFilesDir(null).getAbsolutePath()+"/"+"newversion");
//        if(!file.exists()){
//            file.mkdir();
//        }
//    }
//    public static boolean skin0IsInDisk() {
//        int skinId = 0;
////        File file = Environment.getExternalStorageDirectory();
////        file = new File(file, "/MessageFiles");
//        File file = new File(getSkinDir());
//        file.mkdirs();
//        File zipFile = new File(file, skinId + ".zip");
//        File setupFile = new File(file+"/"+skinId, "setup.txt");
////        File onePng = new File(file+"/"+skinId, "body"+.oipc);
//        if(skinId == 0){//默认皮肤的压缩包在安装文件里
//            if (setupFile.exists()){
//                skin0IsInDisk = true;
//                Log.w("SkinCheck","check once");
//                return true;
//            }
//        }else{
//            if (zipFile.exists() && setupFile.exists()) return true;
//        }
//        return false;
//    }
//
//
//    /**复制默认皮肤到本地*/
//    //is=context.getClass().getClassLoader().getResourceAsStream("assets/"+names[i]);
//    public static void copyZipFileToDisk(final Context context,final String zipPath, final int skinId){
//        Log.w("SkinCheck","copyZipFileToDisk "+skin0IsUnZipping+" running once");
//        if(skin0IsUnZipping)return;
//        skin0IsUnZipping = true;
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//                    InputStream is   =context.getClass().getClassLoader().getResourceAsStream(zipPath);
////					File        file = Environment.getExternalStorageDirectory();
////					file = new File(file, "/MessageFiles");
//                    File file = new File(getSkinDir());
//					file.mkdirs();
//					file = new File(file, skinId+".zip");
//                    FileOutputStream os = new FileOutputStream(file);
//                    int len = 0;
//                    byte[] buffer = new byte[1024];
//                    while ((len = is.read(buffer)) != -1){
//                        os.write(buffer,0,len);
//                        os.flush();
//                    }
//                    is.close();
//                    os.close();
//					if (file.exists()) {
//						Uri uri =  Uri.fromFile(file);// Uri.fromFile(path)这个方法能得到文件的URI
//						ZipUtil.unzip(file.getPath(),file.getPath().replace(skinId+".zip","")+"/"+skinId);
//                        if(skinUnzipListner!=null){
//                            skinUnzipListner.onSuccess();
//                        }
//					}
//                    skin0IsUnZipping  = false;
//				} catch (FileNotFoundException e) {
//					 if (BuildConfig.DEBUG) Log.e("SkinCheck","copyZipFileToDisk FileNotFoundException"+e.toString());
//                    skin0IsUnZipping  = false;
//				} catch (IOException e) {
//                     if (BuildConfig.DEBUG) Log.e("SkinCheck","copyZipFileToDisk IOException"+e.toString());
//                    skin0IsUnZipping  = false;
//				}
//			}
//		}).start();
//    }
//    /**皮肤再次解压到本地*/
//    private static int UnZippingSkinId = -1;//-1未在解压
//    public static void reUnZipFileToDisk(final Context context,final String zipFile, final int skinId){
//        if(skinId == UnZippingSkinId)return;//正在解压同一个包
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    UnZippingSkinId  = skinId;
//                    File file = new File(zipFile);
//                    if (file.exists()) {
//                        Uri uri =  Uri.fromFile(file);// Uri.fromFile(path)这个方法能得到文件的URI
//                        ZipUtil.unzip(file.getPath(),file.getPath().replace(skinId+".zip","")+"/"+skinId);
//                    }
//                    UnZippingSkinId  = -1;
//                }  catch (Exception e) {
//                     if (BuildConfig.DEBUG) Log.e("SkinCheck","copyZipFileToDisk IOException"+e.toString());
//                    UnZippingSkinId  = -1;
//                }
//            }
//        }).start();
//    }
//    /**皮肤删除*/
//    public static void skinDeleteInDisk(int skinId) {
////        File file = Environment.getExternalStorageDirectory();
////        file = new File(file, "/MessageFiles");
//        File file;
//        file = new File(getSkinDir());
//        file.mkdirs();
//        //删zip
//        File zipFile = new File(file, skinId + ".zip");
//        zipFile.delete();
//        //删目录
//        File dirFile = new File(file+"/"+skinId);
//        RecursionDeleteFile(dirFile);
//    }
//    /**皮肤删除*/
//    public static void skinDeleteInDisk(int skinId,int type) {
////        File file = Environment.getExternalStorageDirectory();
////        file = new File(file, "/MessageFiles");
//        File file;
//        if(type==1){
//            file = new File(getSkinDir());
//        }else{
//            file = new File(getSkinBgDir());
//        }
//        file.mkdirs();
//        //删zip
//        File zipFile = new File(file, skinId + ".zip");
//        zipFile.delete();
//        //删目录
//        File dirFile = new File(file+"/"+skinId);
//        RecursionDeleteFile(dirFile);
//    }
//    /**
//     * 递归删除文件和文件夹
//     * @param file    要删除的根目录
//     */
//    private static void RecursionDeleteFile(File file){
//        if(file.isFile()){
//            file.delete();
//            return;
//        }
//        if(file.isDirectory()){
//            File[] childFile = file.listFiles();
//            if(childFile == null || childFile.length == 0){
//                file.delete();
//                return;
//            }
//            for(File f : childFile){
//                RecursionDeleteFile(f);
//            }
//            file.delete();
//        }
//    }
//    public interface  SkinUnzipListner{
//        void onSuccess();
//    }
//    public static void setSkinUnzipListner(SkinUnzipListner zipListner){
//        skinUnzipListner=zipListner;
//    }
//    private static SkinUnzipListner skinUnzipListner;


}
