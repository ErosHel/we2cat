package com.we2cat.filter

import com.intellij.execution.filters.Filter
import com.intellij.openapi.project.Project
import com.we2cat.panel.logPrintln
import com.we2cat.utils.Sql_Parameters
import com.we2cat.utils.Sql_Preparing
import com.we2cat.utils.Sql_StartWith
import com.we2cat.utils.sqlJoint

/**
 * mybatis log filter
 *
 * Created by hel on 2020/12/16 14:23
 */
class MybatisLogFilter(private val project: Project) : Filter {

    private var preparingLine = ""

    private var parametersLine = ""

    override fun applyFilter(line: String, entireLength: Int): Filter.Result? {
        if (line.isNotBlank()) {
            if (line.startsWith(Sql_StartWith)) return null
            if (line.contains(Sql_Preparing)) preparingLine = line.split(Sql_Preparing)[1]
            if (line.contains(Sql_Parameters)) parametersLine = line.split(Sql_Parameters)[1]
            if (preparingLine.isNotBlank() && "" != parametersLine) {
                logPrintln(project.basePath!!, sqlJoint(preparingLine, parametersLine))
                preparingLine = ""
                parametersLine = ""
            }
        }
        return null
    }

}