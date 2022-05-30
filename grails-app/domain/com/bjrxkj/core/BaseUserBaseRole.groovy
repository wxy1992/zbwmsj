package com.bjrxkj.core

import grails.gorm.DetachedCriteria
import org.codehaus.groovy.util.HashCodeHelper

class BaseUserBaseRole implements Serializable {

	private static final long serialVersionUID = 1

    BaseUser baseUser
	BaseRole baseRole

	@Override
	boolean equals(other) {
		if (!(other instanceof BaseUserBaseRole)) {
			return false
		}

		other.baseUser?.id == baseUser?.id &&
				other.baseRole?.id == baseRole?.id
	}


	@Override
	int hashCode() {
	    int hashCode = HashCodeHelper.initHash()
        if (baseUser) {
            hashCode = HashCodeHelper.updateHash(hashCode, baseUser.id)
		}
		if (baseRole) {
		    hashCode = HashCodeHelper.updateHash(hashCode, baseRole.id)
		}
		hashCode
	}

	static BaseUserBaseRole get(String baseUserId, String baseRoleId) {
		criteriaFor(baseUserId, baseRoleId).get()
	}

	static boolean exists(String baseUserId, String baseRoleId) {
		criteriaFor(baseUserId, baseRoleId).count()
	}

	static DetachedCriteria criteriaFor(String baseUserId, String baseRoleId) {
		BaseUserBaseRole.where {
			baseUser == BaseUser.get(baseUserId) &&
			baseRole == BaseRole.get(baseRoleId)
		}
	}

	static BaseUserBaseRole create(BaseUser baseUser, BaseRole baseRole, boolean flush = false) {
		def instance = new BaseUserBaseRole(baseUser: baseUser, baseRole: baseRole)
		instance.save(flush: flush)
		instance
	}

	static boolean remove(BaseUser u, BaseRole r) {
		if (u != null && r != null) {
			BaseUserBaseRole.where { baseUser == u && baseRole == r }.deleteAll()
		}
	}

	static int removeAll(BaseUser u) {
		u == null ? 0 : BaseUserBaseRole.where { baseUser == u }.deleteAll() as int
	}

	static int removeAll(BaseRole r) {
		r == null ? 0 : BaseUserBaseRole.where { baseRole == r }.deleteAll() as int
	}

	static constraints = {
	    baseUser nullable: false
		baseRole nullable: false, validator: { BaseRole r, BaseUserBaseRole ur ->
			if (ur.baseUser?.id) {
				if (BaseUserBaseRole.exists(ur.baseUser.id, r.id)) {
				    return ['userRole.exists']
				}
			}
		}
	}

	static mapping = {
		id composite: ['baseUser', 'baseRole']
		version false
	}
}
