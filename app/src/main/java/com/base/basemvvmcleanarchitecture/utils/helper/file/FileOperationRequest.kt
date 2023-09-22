package com.base.basemvvmcleanarchitecture.utils.helper.file

data class FileOperationRequest(
    val fileName: String,
    val fileExtension: FileExtension,
    val directoryType: DirectoryType
)