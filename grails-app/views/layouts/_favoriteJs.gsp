<%@ page import="com.bjrxkj.cms.behaviour.Favourite" %>
<%
    def baseUserId=sec.loggedInUserInfo('field':"id")?:'-1';
    def ifLikeNews=Favourite.createCriteria().count {
        eq('type','news')
        eq('contentId',newsInstance.id.toString())
        eq('baseUser.id',baseUserId)
    }

    def likeCommentstr=Favourite.createCriteria().list {
        projections {
            property('contentId')
        }
        eq('type','comment')
        like('contentId',"${newsInstance.id}-%")
        eq('baseUser.id',baseUserId)
    }.join(',')
%>
<style type="text/css">
.neirong *{
    font-size: 16px;
}
.commentTextarea{
    border-top:3px #666 solid;
    display: flex;
    justify-content: space-between;
    background: #fff;
    padding: 20px;
}
.commentTextarea .submitComment{
    width: 50px;
    height: 30px;
}
.listContent ul{
    border-top:3px #666 solid;
    padding: 30px 0px;
    background: #fff;
    display: none;
}
.listContent li{
    min-height: 100px;
    display: flex;
    justify-content: space-between;
    padding: 20px 30px;
    border-bottom: 1px solid #e8e8e8;
    text-align: left;
}
.listContent li p{
    line-height: 32px;
    text-align: left;
    width: 85%;
}
.listContent li span{
    text-align: left;
    color: #666;
    display: block;
}
img.headImg{
    width: 50px;
    height: 50px;
    border-radius: 50%;
    margin-right: 30px;
}
.listContent li i.likeComment{
    background-image: url(${request.contextPath}/images/sprite.png);
    background-repeat: no-repeat;
    background-position: 0 1px;
    width: 20px;
    height: 20px;
    cursor: pointer;
}
.listContent li i.likeComment.liked{
    background-position: 0 -49px;
}
.collectionIcon.liked{
    color:#d39e00;
}
.collectionIcon{
    font-size: 16px;
    display: inline-block;
    cursor: pointer;
}
.myFavoriteUl{
    display: flex;
    justify-content: left;
    padding:0px;
}
.myFavoriteUl li{
    width: 100px;
    list-style-type: none;
    padding: 0px 10px;
}
.myFavoriteUl li a{
    font-size: 20px;
}
.myFavoriteUl li a.active{
    color: #d39e00;
}
</style>
<script>
    $(function(){
        $('body').on('click','.myFavoriteUl li a',function () {
            var _this=$(this);
            $('.myFavoriteUl li a').removeClass('active');
            _this.addClass('active');
            $('.listContent ul').hide();
            $('#'+_this.attr('data-ulname')).show();
            if(_this.attr('data-ulname')=='commentTable'){
                commentJson({newsId:${newsInstance.id},offset:0,baseUserId:''},'#'+_this.attr('data-ulname'));
            }else if(_this.attr('data-ulname')=='myComment'){
                commentJson({newsId:${newsInstance.id},offset:0,baseUserId:'${baseUserId}'},'#'+_this.attr('data-ulname'));
            }else if(_this.attr('data-ulname')=='myCollection'){
                collectionJson({offset:0,baseUserId:'${baseUserId}'},'#'+_this.attr('data-ulname'));
            }
        });
        $('.myFavoriteUl li a.active').click();
        $('body').on('click','i.likeComment,.collectionIcon',function () {
            <sec:ifNotLoggedIn>
            window.location.href="${request.contextPath}/login";
            </sec:ifNotLoggedIn>
            <sec:ifLoggedIn>
            var optype=$(this).hasClass('liked')?'cancel':'like';
            var type=$(this).attr('data-type');
            var contentId=$(this).attr('data-contentId');
            var _this=$(this);
            $.post("${request.contextPath}/web/commentOrNewsLike",{type:type,optype:optype,contentId:contentId},function(data,status){
                if(data=='true'){
                    if(optype=='cancel'){
                        _this.removeClass('liked');
                    }else{
                        _this.addClass('liked');
                    }
                }
            });
            </sec:ifLoggedIn>
        });
    });

    function submitComment(){
        <sec:ifNotLoggedIn>
            window.location.href="${request.contextPath}/login";
        </sec:ifNotLoggedIn>
        <sec:ifLoggedIn>
        var obj=new Object();
        obj.content=$('textarea[name=content]').val();
        obj.newsId="${newsInstance.id}";
        if (obj.newsId && obj.content) {
            $.post("${request.contextPath}/web/submitComment",obj,function(data,status){
                if(data.result){
                    alert("评论成功");
                    $('textarea[name=content]').val('');
                    commentJson({newsId:${newsInstance.id},offset:0,baseUserId:''},'#commentTable');
                }
            },"json");
        }else{
            alert("评论内容不能为空");
        }
        </sec:ifLoggedIn>
    }
    //评论列表
    function commentJson(obj,showbox){
        $.post("${request.contextPath}/web/commentJson",obj,function(data,status){
            var rows=data.rows;
            if(obj.offset==0){
                $(showbox).html('');
            }
            for(var i=0;i<rows.length;i++){
                var data_contentId="${newsInstance.id}-"+rows[i].id;
                var ifliked=($('#likeCommentstr').val().indexOf(data_contentId)>-1)?'liked':'';
                var str="<li><img class='headImg' src='${request.contextPath}/web/userHeadimg/"+rows[i].baseUserId+"'/><p>";
                if(showbox=='#myComment'){
                    str+=" #<a class='text-info' href='${request.contextPath}/news/detail/"+rows[i].newsId+".html'>"+rows[i].title+"</a># ";
                }
                str+=rows[i].content+"<span>"+rows[i].dateCreated+"</span></p><i class='likeComment "+ifliked+"' data-type='comment' data-contentId='"+data_contentId+"'></li>";
                $(showbox).append(str);
            }
        },"json");
    }
    //收藏列表
    function collectionJson(obj,showbox){
        console.log(obj);
        $.post("${request.contextPath}/web/collectionJson",obj,function(data,status){
            var rows=data.rows;
            if(obj.offset==0){
                $(showbox).html('');
            }
            for(var i=0;i<rows.length;i++){
                var str="<li><img class='headImg' src='${request.contextPath}/web/userHeadimg/"+rows[i].baseUserId+"'/><p>";
                str+=" #<a class='text-info' href='${request.contextPath}/news/detail/"+rows[i].newsId+".html'>"+rows[i].title+"</a># ";
                str+=rows[i].comment+"<span>"+rows[i].dateCreated+"</span></p></li>";
                $(showbox).append(str);
            }
        },"json");
    }

</script>
<div class="row col-lg-12">
    <input type="hidden" id="likeCommentstr" value="${likeCommentstr}">
    <div class="col-md-12 commentTextarea">
        <img class="headImg" src="${request.contextPath}/web/userHeadimg/${baseUserId}"/>
        <textarea name="content" class="form-control" rows="5" placeholder="我想说..."></textarea>
    </div>
    <div class="col-md-12 text-right">
        <button class="btn btn-warning btn-lg submitComment" onclick="submitComment();">发布</button>
%{--        <i data-type="news" data-contentId="${newsInstance.id}" class="collectionIcon <g:if test="${ifLikeNews>0}">liked</g:if>"></i>--}%
        <a data-type="news" data-contentId="${newsInstance.id}" class="collectionIcon <g:if test="${ifLikeNews>0}">liked</g:if>">收藏</a>
    </div>

    <!--评论列表-->
    <div class="row">
    <div class="col-md-12">
        <ul class="myFavoriteUl">
            <li><a href="javascript://" class="active" data-ulname="commentTable">最新评论</a></li>
            <sec:ifLoggedIn>
                <li><a href="javascript://" class="" data-ulname="myComment">我的评论</a></li>
                <li><a href="javascript://" class="" data-ulname="myCollection">我的收藏</a></li>
            </sec:ifLoggedIn>
        </ul>
        <div class="listContent">
            <ul id="commentTable"> </ul>
            <ul id="myComment"> </ul>
            <ul id="myCollection"> </ul>
        </div>
    </div>
    </div>

</div>