package com.we2cat.filter

import com.intellij.execution.filters.Filter
import com.intellij.openapi.project.Project
import com.we2cat.utils.mybatisSqlLog

/**
 * mybatis log filter
 *
 * Created by hel on 2020/12/16 14:23
 */
class ConsoleFilter(private val project: Project) : Filter {

    override fun applyFilter(line: String, entireLength: Int): Filter.Result? {
        mybatisSqlLog(project.basePath!!, line)
        return null
    }

}