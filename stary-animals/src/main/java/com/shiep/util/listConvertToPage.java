package com.shiep.util;

import java.util.List;

public class listConvertToPage {
    /**
     * 将 list 转换为 分页Page
     *
     * @param list     list
     * @param total    总记录数
     * @param pageable 分页信息
     * @return
     * @author YB
     * @Date 2019/11/8 14:26
     */
    /*public static <T> Page<T> listConvertToPage(List<T> list, long total, Pageable pageable) {
        // 当前页第一条数据在List中的位置
        int start = (pageable.getPageNumber() - 1) * pageable.getPageSize();
        // 当前页最后一条数据在List中的位置
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (pageable.getPageSize() * pageable.getPageNumber());
        //System.out.println(start + " , " + end + ", size:" + list.size());
        //配置分页数据
        return new Page<>(list.subList(start, end), total, pageable);
    }*/
}
