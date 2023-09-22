package com.base.domain.models.file

open class GroupFile {
    private var id: Long? = null
    private var parentId: Int? = null
    private var name: String? = null
    private var createDate: Long? = null

    constructor()

    constructor(id: Long?) {
        this.id = id
    }

    constructor(id: Long?, parentId: Int?, name: String?, createDate: Long?) {
        this.id = id
        this.parentId = parentId
        this.name = name
        this.createDate = createDate
    }

    fun getId(): Long? {
        return id
    }

    fun setId(id: Long?) {
        this.id = id
    }

    fun getParentId(): Int? {
        return parentId
    }

    fun setParentId(parentId: Int?) {
        this.parentId = parentId
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getCreateDate(): Long? {
        return createDate
    }

    fun setCreateDate(createDate: Long?) {
        this.createDate = createDate
    }
}