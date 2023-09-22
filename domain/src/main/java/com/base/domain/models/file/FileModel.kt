package com.base.domain.models.file

open class FileModel {
    companion object {
        const val FILE_DIR = 0
        const val FILE_FILE = 1
    }

    private var name: String? = null
    private var path: String? = null
    private var fileType: Int? = null
    private var numOfItems: Int = 0

    constructor()

    constructor(name: String?, path: String?, fileType: Int?) {
        this.name = name
        this.path = path
        this.fileType = fileType
    }

    constructor(name: String?, path: String?, fileType: Int?, numOfItems: Int) {
        this.name = name
        this.path = path
        this.fileType = fileType
        this.numOfItems = numOfItems
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getPath(): String? {
        return path
    }

    fun setPath(path: String) {
        this.path = path
    }

    fun getFileType(): Int? {
        return fileType
    }

    fun setFileType(fileType: Int) {
        this.fileType = fileType
    }

    fun getNumOfItems(): Int = numOfItems

    fun setNumOfItems(numOfItems: Int) {
        this.numOfItems = numOfItems
    }
}