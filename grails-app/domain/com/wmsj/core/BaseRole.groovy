package com.wmsj.core


class BaseRole {
	String id
	String authority
	String description
	String catalogstr
	String name
	static mapping = {
		cache true
		id generator: 'uuid'
		authority column: 'authority_str'
	}

	static constraints = {
		authority blank: false, unique: true
		description blank: false
		name blank: false
		catalogstr nullable: true
	}
	String toString(){
		return authority;
	}
	List catalogList(){
		this.catalogstr.tokenize(',').toList()
	}
}
