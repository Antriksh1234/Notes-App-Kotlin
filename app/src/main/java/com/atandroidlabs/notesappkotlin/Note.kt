package com.atandroidlabs.notesappkotlin

class Note {
    var title: String? = ""
    var content: String? = ""
    var id: Int? = 0
    constructor(id: Int?, title: String?, content: String?) {
        this.id = id
        this.title = title
        this.content = content
    }

    fun getTitleOfNote(): String? {
        return title
    }

    fun getContentOfNote(): String? {
        return content
    }

}