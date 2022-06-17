package com.wmsj.cms

//import VideoConvert
import grails.converters.JSON
//import it.sauronsoftware.jave.AudioAttributes
//import it.sauronsoftware.jave.Encoder
//import it.sauronsoftware.jave.EncoderException
//import it.sauronsoftware.jave.EncodingAttributes
//import it.sauronsoftware.jave.InputFormatException
//import it.sauronsoftware.jave.VideoAttributes

/*YYH-->后台附件管理
* */
class AttachmentController {

    def commonService;
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:['POST','GET'], save:'POST', update:'POST']

    def attachmentSearch(def params){
        return Attachment.whereLazy {
            if(params.type){
                eq('type',params.type.toInteger())
            }
            if(params.title){
                or{
                    like("name","%"+params.title.trim()+"%")
                    news{
                        like("title","%"+params.title.trim()+"%")
                    }
                }
            }
            if(params.beginDate){
                ge("dateCreated",Date.parse("yyyy-MM-dd HH:mm:ss",params.beginDate+" 00:00:00"))
                le("dateCreated",Date.parse("yyyy-MM-dd HH:mm:ss",params.beginDate+" 23:59:59"))
            }
        }
    }
    def list = {}
    def listJson={
        if(!params.max) params.max='10'
        if(!params.offset) params.offset ='0'
        def objs=attachmentSearch(params).list(
                max: params.max.toInteger(),
                offset: params.offset.toInteger(),{
            order('id','desc')
            order('dateCreated','desc')
                })
        def objsCount=attachmentSearch(params).count();
        def map=[:],list=[];
        int i=0;
        objs.each{
            def m=[:];
            i++;
            m.xh=i
            m.newsId=it.newsId;
            m.name=it.name;
            m.title=it.news?.title?:it.name;
            m.dateCreated=it.dateCreated.format("yyyy-MM-dd");
            m.id=it.id;
            m.type=it.type;
            list<<m;
        }
        map.rows=list;
        map.total=objsCount;
        render "${map as JSON}";
    }
    //删除附件
    def deleteAttachment(){
        def map=[:];
        map.result=false;
        map.message="删除失败";
        try{
            def attachmentInstance=Attachment.get(params.id);
            File filepath=new File(attachmentInstance.filepath);
            if(filepath.exists()){
                filepath.delete();
            }
            attachmentInstance.delete(flush: true);
            if(attachmentInstance.hasErrors()){
                map.result=false
                map.message="删除失败"
            }
            map.result=true
            map.message="删除成功"
        }catch(e){
            log.error(e.message);
        }
        render "${map as JSON}";
}
    /**
     * 上传附件
     */
    def fileUpload(){
        def dirPath=grailsApplication.config.project.setting.attachmentPath+File.separator+params.dataType;
        def map=[:]; map.status=0;map.message="网络错误，请重试";map.file_id='';
        try{
            def aFile=request.getFile("file_data");
            map=commonService.fileUpload(dirPath,aFile,params);
        }catch(e){
            log.error(e.message);
        }
        render "${map as JSON}";
    }

    /**
     * 上传视频
     */
    def videoUpload(){
        def map=[:];map.status=0;map.message="网络错误，请重试";
        def vFile= request.getFile('file_data');
        if( vFile && !vFile.isEmpty()){
            def date=new Date().format('yyyyMMddHHmmss');
            def filename=vFile.getOriginalFilename();
            def name="${date}_${filename}";
            def dir_path=request.getRealPath("/videos/");
            if(!new File(dir_path).exists()){
                new File(dir_path).mkdirs();
            }
            def path=request.getRealPath("/videos/"+name);
            vFile.transferTo(new File(path));
            map.status=2;
            map.message="上传成功";
            map.videoPath=name;
        }else{
            map.message="文件为0字节文件！";
        }
        render "${map as JSON}";
    }

    def deleteVideo(){
        def map=[:];
        map.result=false;
        map.message="网络错误，请重试！";
        if(params.newsId){
            def newsInstance=News.get(params.newsId.toLong());
            newsInstance.videoPath=null;
            if(newsInstance.save(flush: true)){
                map.result=true;
                map.message="操作成功";
            }
        }else{
            map.message="缺少参数！";
        }
        render "${map as JSON}";
    }
    def videoConvert(){
        VideoConvert.execute();
//
//        File source = new File("D:\\attachment\\22.flv");
//        File target = new File("D:\\attachment\\22.mp4");
//        AudioAttributes audio = new AudioAttributes();
//        audio.setCodec(AudioAttributes.DIRECT_STREAM_COPY);
//        audio.setBitRate(new Integer(128000));
//        audio.setSamplingRate(new Integer(44100));
//        audio.setChannels(new Integer(2));
//        VideoAttributes video = new VideoAttributes();
//        video.setBitRate(new Integer(160000));
//        video.setFrameRate(new Integer(15));
//        video.setCodec("mpeg4");
//        video.setCodec(VideoAttributes.DIRECT_STREAM_COPY);
//        EncodingAttributes attrs = new EncodingAttributes();
//        attrs.setFormat("mp4");
//        attrs.setAudioAttributes(audio);
//        attrs.setVideoAttributes(video);
//        Encoder encoder = new Encoder();
//        try {
//
//            encoder.encode(source, target, attrs);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (InputFormatException e) {
//            e.printStackTrace();
//        } catch (EncoderException e) {
//            e.printStackTrace();
//        }
        render 'ok'
    }
}
