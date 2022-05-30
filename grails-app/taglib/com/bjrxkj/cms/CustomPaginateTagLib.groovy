package com.bjrxkj.cms

import org.springframework.web.servlet.support.RequestContextUtils

class CustomPaginateTagLib implements com.opensymphony.module.sitemesh.RequestConstants {

	//def out // to facilitate testing
/**
	 * Creates next/previous links to support pagination for the current controller
	 *
	 * <g:paginate total="${Account.count()}" />
	 */
	 def paginate2search = { attrs ->
         String url1=""
         String url2=""
         String url3=""
         String url4=""
         String url5=""
         String htmlStr
		def writer = out
	    if(attrs.total == null)
	        throwTagError("Tag [paginate] is missing required attribute [total]")

		def messageSource = grailsAttributes.getApplicationContext().getBean("messageSource")
		def locale = RequestContextUtils.getLocale(request)

		def total = attrs.total.toInteger()
		def action = (attrs.action ? attrs.action : (params.action ? params.action : "list"))
		def offset = params.offset?.toInteger()
		def max = params.max?.toInteger()
		def maxsteps = (attrs.maxsteps ? attrs.maxsteps.toInteger() : 10)

		if(!offset) offset = (attrs.offset ? attrs.offset.toInteger() : 0)
		if(!max) max = (attrs.max ? attrs.max.toInteger() : 10)

		def linkParams = [offset:offset - max, max:max]
		def postParams = [max:max]
		if(params.sort) linkParams.sort = params.sort
		if(params.order) linkParams.order = params.order
		
		// to change for search page
		if(attrs.params && attrs.params=="search"){  
			 params.remove('offset')
		     linkParams.putAll(params)
			 postParams.putAll(params)
			 postParams.remove('action')
			 postParams.remove('controller')
		}
		//if(attrs.params) linkParams.putAll(attrs.params)

		def linkTagAttrs = [action:action]
		if(attrs.controller) {
			linkTagAttrs.controller = attrs.controller
		}
		if(attrs.id!=null) {
			linkTagAttrs.id = attrs.id
		}
		linkTagAttrs.params = linkParams

		// determine paging variables
		def steps = maxsteps > 0
		int currentstep = (offset / max) + 1
		int firststep = 1
		int laststep = Math.round(Math.ceil(total / max))

		// display previous link when not on firststep
		String totalStr= messageSource.getMessage('default.paginate.total', null, '共：', locale)
         String noStr= messageSource.getMessage('default.paginate.pageNo', null, '页码：', locale)
		if(currentstep > firststep) {
			linkTagAttrs.class = 'prevLink'
				url1=link(linkTagAttrs.clone()) {
				(attrs.prev ? attrs.prev : messageSource.getMessage('paginate.prev', null, messageSource.getMessage('default.paginate.prev', null, 'Previous', locale), locale))
			 }
		}

		// display steps when steps are enabled and laststep is not firststep
		if(steps && laststep > firststep) {
			def formaction=request.getRequestURI()-".dispatch"-"/grails"
			
			
			
			
			
			htmlStr="""
			<input id="laststep" value="${laststep}" type="hidden"/>
			<input id="paginateTotal" value="${total}" type="hidden"/>
			<input id="paginateMax" value="${max}" type="hidden"/>
			<input name="paginateNum" id="paginateNum" value="${currentstep}" style="width:20px"/>
			<input onclick="submitPaginate();" type="button" value="转到" class="btn btn-primary" style="width:50px"/>
				<form action="${formaction}" method="post" id="paginateForm">
			
				"""
			postParams.each{
					htmlStr=htmlStr+"""
					<input name="${it.key}"  value="${it.value}" type="hidden"/>
					
					
					"""
			
			}
				htmlStr=htmlStr+"""
					<input name="offset" id="paginateOffset" value="" type="hidden"/>
			
					</form>
	
				"""
				
			
			linkTagAttrs.class = 'step'

			// determine begin and endstep paging variables
			int beginstep = currentstep - Math.round(maxsteps / 2) + (maxsteps % 2)
			int endstep = currentstep + Math.round(maxsteps / 2) - 1

			if(beginstep < firststep) {
				beginstep = firststep
				endstep = maxsteps
			}
			if(endstep > laststep) {
				beginstep = laststep - maxsteps + 1
				if(beginstep < firststep) {
					beginstep = firststep
				}
				endstep = laststep
			}
			// display paginate steps
			(beginstep..endstep).each { i ->
				if(currentstep == i) {
					
					url5= "<span class=\"currentStep\" style=\"text-align: right\">${totalStr}&nbsp;${attrs.total}&nbsp;条&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${noStr}&nbsp;${i}/${laststep.toString()}</span>"
				}
				else {
					linkParams.offset = (i - 1) * max
					//writer << link(linkTagAttrs.clone()) {i.toString()}
				}
			}
			// display firststep link when beginstep is not firststep
		//	if(beginstep > firststep) {
				linkParams.offset = 0
				//writer <<"&nbsp;&nbsp;&nbsp;&nbsp;"
				String firstStr= messageSource.getMessage('default.paginate.home', null, '首页', locale)
				url2= link(linkTagAttrs.clone()) { firstStr}
				//writer <<"&nbsp;&nbsp;&nbsp;&nbsp;"
				//writer << '<span class="step">..</span>'
		//	}

		

			// display laststep link when endstep is not laststep
		//	if(endstep < laststep) {
				//writer << '<span class="step">..</span>'
				linkParams.offset = (laststep -1) * max
				//writer <<"&nbsp;&nbsp;&nbsp;&nbsp;"
				String lastStr= messageSource.getMessage('default.paginate.last', null, '末页', locale)
				url4= link(linkTagAttrs.clone()) { lastStr }
				//writer <<"&nbsp;&nbsp;&nbsp;&nbsp;"
			//}
		}
		else{
			url5="<span class=\"currentStep\">${totalStr}&nbsp;${attrs.total}&nbsp;条&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${noStr}&nbsp;1/1</span>"
		}

		// display next link when not on laststep
		if(currentstep < laststep) {
			linkTagAttrs.class = 'nextLink'
			linkParams.offset = offset + max
			url3= link(linkTagAttrs.clone()) {
				(attrs.next ? attrs.next : messageSource.getMessage('paginate.next', null, messageSource.getMessage('default.paginate.next', null, 'Next', locale), locale))
			}
		}
		writer << url5
		if(url2){
			writer << "&nbsp;&nbsp;&nbsp;&nbsp;"
			writer << url2
			
		}
		if(url1){
			writer << "&nbsp;&nbsp;&nbsp;&nbsp;"
			writer << url1
			
		}
		
		if(url3){
			writer << "&nbsp;&nbsp;&nbsp;&nbsp;"
			writer << url3
			
		}
		
		if(url4){
			writer << "&nbsp;&nbsp;&nbsp;&nbsp;"
			writer << url4
			
		}
		def scriptStr="""
		<script language="javascript">
			
		function submitPaginate(){
			var e1 =document.getElementById("paginateOffset");
			var e2 =document.getElementById("paginateNum");
			var e3 =document.getElementById("paginateTotal");
			var e4 =document.getElementById("paginateMax");
			var e5 =document.getElementById("laststep");
			var pattern=/^[0-9]*[1-9][0-9]*\$/;
			if(!pattern.test(e2.value)){
			
				e1.value='0';
			}
			else{
				if(parseInt(e2.value)>parseInt(e5.value))
				e2.value=e5.value;
				
				e1.value=parseInt(e4.value)*(parseInt(e2.value)-1);
			}
			document.getElementById('paginateForm').submit();
		}
		
		
		</script>
		
		
		"""
		
	
	    writer << scriptStr
        if(htmlStr){
            writer << htmlStr
        }
	}
	 def paginate2searchEN = { attrs ->
         String url1=""
         String url2=""
         String url3=""
         String url4=""
         String url5=""
         String htmlStr
		 def writer = out
		 if(attrs.total == null)
			 throwTagError("Tag [paginate] is missing required attribute [total]")
 
		 def messageSource = grailsAttributes.getApplicationContext().getBean("messageSource")
		 def locale = RequestContextUtils.getLocale(request)
 
		 def total = attrs.total.toInteger()
		 def action = (attrs.action ? attrs.action : (params.action ? params.action : "list"))
		 def offset = params.offset?.toInteger()
		 def max = params.max?.toInteger()
		 def maxsteps = (attrs.maxsteps ? attrs.maxsteps.toInteger() : 10)
 
		 if(!offset) offset = (attrs.offset ? attrs.offset.toInteger() : 0)
		 if(!max) max = (attrs.max ? attrs.max.toInteger() : 10)
 
		 def linkParams = [offset:offset - max, max:max]
		 def postParams = [max:max]
		 if(params.sort) linkParams.sort = params.sort
		 if(params.order) linkParams.order = params.order
		 
		 // to change for search page
		 if(attrs.params && attrs.params=="search"){
			  params.remove('offset')
			  linkParams.putAll(params)
			  postParams.putAll(params)
			  postParams.remove('action')
			  postParams.remove('controller')
		 }
		 //if(attrs.params) linkParams.putAll(attrs.params)
 
		 def linkTagAttrs = [action:action]
		 if(attrs.controller) {
			 linkTagAttrs.controller = attrs.controller
		 }
		 if(attrs.id!=null) {
			 linkTagAttrs.id = attrs.id
		 }
		 linkTagAttrs.params = linkParams
 
		 // determine paging variables
		 def steps = maxsteps > 0
		 int currentstep = (offset / max) + 1
		 int firststep = 1
		 int laststep = Math.round(Math.ceil(total / max))
 
		 // display previous link when not on firststep
		 String totalStr= "Total:"
         String noStr= "Page:"
		 if(currentstep > firststep) {
			 linkTagAttrs.class = 'prevLink'
				 url1=link(linkTagAttrs.clone()) {
				 "Previous"
					 }
		 }
 
		 // display steps when steps are enabled and laststep is not firststep
		 if(steps && laststep > firststep) {
			 def formaction=request.getRequestURI()-".dispatch"-"/grails"
			 
			 
			 
			 
			 
			 htmlStr="""
			 <input id="laststep" value="${laststep}" type="hidden"/>
			 <input id="paginateTotal" value="${total}" type="hidden"/>
			 <input id="paginateMax" value="${max}" type="hidden"/>
			 <input name="paginateNum" id="paginateNum" value="${currentstep}" style="width:20px"/>
			 <input onclick="submitPaginate();" type="button" value="转到" class="btn btn-primary" style="width:50px"/>
				 <form action="${formaction}" method="post" id="paginateForm">
			 
				 """
			 postParams.each{
					 htmlStr=htmlStr+"""
					 <input name="${it.key}"  value="${it.value}" type="hidden"/>
					 
					 
					 """
			 
			 }
				 htmlStr=htmlStr+"""
					 <input name="offset" id="paginateOffset" value="" type="hidden"/>
			 
					 </form>
	 
				 """
				 
			 
			 linkTagAttrs.class = 'step'
 
			 // determine begin and endstep paging variables
			 int beginstep = currentstep - Math.round(maxsteps / 2) + (maxsteps % 2)
			 int endstep = currentstep + Math.round(maxsteps / 2) - 1
 
			 if(beginstep < firststep) {
				 beginstep = firststep
				 endstep = maxsteps
			 }
			 if(endstep > laststep) {
				 beginstep = laststep - maxsteps + 1
				 if(beginstep < firststep) {
					 beginstep = firststep
				 }
				 endstep = laststep
			 }
			 // display paginate steps
			 (beginstep..endstep).each { i ->
				 if(currentstep == i) {
					 
					 url5= "<span class=\"currentStep\">${totalStr}&nbsp;${attrs.total}&nbsp;&nbsp;&nbsp;&nbsp;${noStr}&nbsp;${i}/${laststep.toString()}</span>"
				 }
				 else {
					 linkParams.offset = (i - 1) * max
					 //writer << link(linkTagAttrs.clone()) {i.toString()}
				 }
			 }
			 // display firststep link when beginstep is not firststep
		 //	if(beginstep > firststep) {
				 linkParams.offset = 0
				 //writer <<"&nbsp;&nbsp;&nbsp;&nbsp;"
				 String firstStr= "Home"
				 url2= link(linkTagAttrs.clone()) { firstStr}
				 //writer <<"&nbsp;&nbsp;&nbsp;&nbsp;"
				 //writer << '<span class="step">..</span>'
		 //	}
 
		 
 
			 // display laststep link when endstep is not laststep
		 //	if(endstep < laststep) {
				 //writer << '<span class="step">..</span>'
				 linkParams.offset = (laststep -1) * max
				 //writer <<"&nbsp;&nbsp;&nbsp;&nbsp;"
				 String lastStr= "Last"
				 url4= link(linkTagAttrs.clone()) { lastStr }
				 //writer <<"&nbsp;&nbsp;&nbsp;&nbsp;"
			 //}
		 }
		 else{
			 url5="<span class=\"currentStep\">${totalStr}&nbsp;${attrs.total}&nbsp;&nbsp;&nbsp;&nbsp;${noStr}&nbsp;1/1</span>"
		 }
 
		 // display next link when not on laststep
		 if(currentstep < laststep) {
			 linkTagAttrs.class = 'nextLink'
			 linkParams.offset = offset + max
			 url3= link(linkTagAttrs.clone()) {
				 "Next"
			 }
		 }
		 writer << url5
		 if(url2){
			 writer << "&nbsp;&nbsp;&nbsp;&nbsp;"
			 writer << url2
			 
		 }
		 if(url1){
			 writer << "&nbsp;&nbsp;&nbsp;&nbsp;"
			 writer << url1
			 
		 }
		 
		 if(url3){
			 writer << "&nbsp;&nbsp;&nbsp;&nbsp;"
			 writer << url3
			 
		 }
		 
		 if(url4){
			 writer << "&nbsp;&nbsp;&nbsp;&nbsp;"
			 writer << url4
			 
		 }
		 def scriptStr="""
		 <script language="javascript">
			 
		 function submitPaginate(){
			 var e1 =document.getElementById("paginateOffset");
			 var e2 =document.getElementById("paginateNum");
			 var e3 =document.getElementById("paginateTotal");
			 var e4 =document.getElementById("paginateMax");
			 var e5 =document.getElementById("laststep");
			 var pattern=/^[0-9]*[1-9][0-9]*\$/;
			 if(!pattern.test(e2.value)){
			 
				 e1.value='0';
			 }
			 else{
				 if(parseInt(e2.value)>parseInt(e5.value))
				 e2.value=e5.value;
				 
				 e1.value=parseInt(e4.value)*(parseInt(e2.value)-1);
			 }
			 document.getElementById('paginateForm').submit();
		 }
		 
		 
		 </script>
		 
		 
		 """
		 
	 
	    writer << scriptStr
         if(htmlStr){
             writer << htmlStr
         }
     }
	
	 /**
		 * Renders a sortable column to support sorting in list views
		 *
		 * Attributes:
		 *
		 * property - name of the property relating to the field
		 * defaultOrder (optional) - default order for the property; choose between asc (default if not provided) and desc
		 * title (optional*) - title caption for the column
		 * titleKey (optional*) - title key to use for the column, resolved against the message source
		 * params (optional) - a map containing request parameters
		 * action (optional) - the name of the action to use in the link, if not specified the list action will be linked
		 *
		 * Attribute title or titleKey is required. When both attributes are specified then titleKey takes precedence,
		 * resulting in the title caption to be resolved against the message source. In case when the message could
		 * not be resolved, the title will be used as title caption.
		 *
		 * Examples:
		 *
		 * <g:sortableColumn property="title" title="Title" />
		 * <g:sortableColumn property="title" title="Title" style="width: 200px" />
		 * <g:sortableColumn property="title" titleKey="book.title" />
		 * <g:sortableColumn property="releaseDate" defaultOrder="desc" title="Release Date" />
		 * <g:sortableColumn property="releaseDate" defaultOrder="desc" title="Release Date" titleKey="book.releaseDate" />
		 */
		def sortableColumn2search = { attrs ->
			def writer = out
			if(!attrs.property)
				throwTagError("Tag [sortableColumn] is missing required attribute [property]")

			if(!attrs.title && !attrs.titleKey)
				throwTagError("Tag [sortableColumn] is missing required attribute [title] or [titleKey]")

			def property = attrs.remove("property")
			def action = attrs.action ? attrs.remove("action") : (params.action ? params.action : "list")

			def defaultOrder = attrs.remove("defaultOrder")
			if(defaultOrder != "desc") defaultOrder = "asc"

			// current sorting property and order
			def sort = params.sort
			def order = params.order

			
			// add sorting property and params to link params
			def linkParams = [sort:property]
			if(params.id) linkParams.put("id",params.id)		

			//xiaopeng add it
			if(params.max) linkParams.max = params.max
			if(params.offset) linkParams.offset = params.offset
			//if(attrs.params) linkParams.putAll(attrs.remove("params"))
			if(attrs.params && attrs.params == "search"){ 
			     linkParams.putAll(params)
			     linkParams.sort = property
			}
			
			// determine and add sorting order for this column to link params
			attrs.class = (attrs.class ? "${attrs.class} sortable" : "sortable")
			if(property == sort) {			
				attrs.class = attrs.class + " sorted " + order
				if(order == "asc") {
					linkParams.order = "desc"
				}
				else {
					linkParams.order = "asc"
				}
			}
			else {
				
				linkParams.order = defaultOrder
			}

			
			// determine column title
			def title = attrs.remove("title")
			def titleKey = attrs.remove("titleKey")
			if(titleKey) {
				if(!title) title = titleKey
				def messageSource = grailsAttributes.getApplicationContext().getBean("messageSource")
				def locale = RequestContextUtils.getLocale(request)
				title = messageSource.getMessage(titleKey, null, title, locale)
			}

			writer << "<th "
			// process remaining attributes
			attrs.each { k, v ->
				writer << "${k}=\"${v.encodeAsHTML()}\" "
			}
			writer << ">${link(action:action, params:linkParams) { title }}</th>"
		}

}