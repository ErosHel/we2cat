package com.we2cat.provider

import com.intellij.execution.filters.ConsoleFilterProvider
import com.intellij.execution.filters.Filter
import com.intellij.openapi.project.Project
import com.we2cat.utils.mybatisSqlLog

/**
 * mybatis log provider
 *
 * Created by hel on 2020/12/16 14:25
 */
class MybatisLogFilterProvider : ConsoleFilterProvider {

    override fun getDefaultFilters(project: Project): Array<Filter> =
        arrayOf(Filter { line, _ -> mybatisSqlLog(project.basePath!!, line);null })

}