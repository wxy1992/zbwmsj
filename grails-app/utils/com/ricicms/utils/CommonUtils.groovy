package com.ricicms.utils

class CommonUtils {
    /**
     * 获取扩展名,不带点
     * @param fileName
     * @return
     */
    public static String getFileExtention(String fileName){
        if(fileName!=null && fileName.length()>0 && fileName.lastIndexOf(".")>-1){
            return fileName.substring(fileName.lastIndexOf(".")+1);
        }
        return "";
    }

    /**
     * --------------------------------------------静态化---------------------------------------
     *将静态文件存放在本地
     *  @param filepath
     * @param str
     */
    public static void saveHtml(String filepath, String str){
        try {
            OutputStreamWriter outs = new OutputStreamWriter(new FileOutputStream(filepath, true), "utf-8");
            outs.write(str);
            System.out.print(str);
            outs.close();
        } catch (IOException e) {
            System.out.println("Error at save html...");
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    def importHtml(){
        String filepath = "D:\\Program Files\\apache-tomcat-8.5.37\\webapps\\sppConsult\\web\\detail\\79.html";
        String url_str = "http://127.0.0.1:8099/sppConsult/web/detail/79?curCategoryId=1";
        URL url = null;
        try {
            url = new URL(url_str);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String charset = "utf-8";
        int sec_cont = 1000;
        try {
            URLConnection url_con = url.openConnection();
            url_con.setDoOutput(true);
            url_con.setReadTimeout(10 * sec_cont);
            url_con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
            InputStream htm_in = url_con.getInputStream();
            String htm_str = inputStream2String(htm_in,charset);
            saveHtml(filepath,htm_str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 输入流转文字
     * @param in_st
     * @param charset
     * @return
     * @throws IOException
     */
    String inputStream2String(InputStream in_st,String charset) throws IOException{
        BufferedReader buff = new BufferedReader(new InputStreamReader(in_st, charset));
        StringBuffer res = new StringBuffer();
        String line = "";
        while((line = buff.readLine()) != null){
            res.append(line+"\n\r");
        }
        return res.toString();
    }

}
