package com.hwz.platform.platform;

/**
 * 分页工具 Created by luoyf on 2016/3/29.
 */
public class Pagination {

    private static final long serialVersionUID     = 1L;

    public static final int   DEFAULT_PAGE_SIZE    = 15;
    public static final int   DEFAULT_CURRENT_PAGE = 1;
    public static final int   DEFAULT_SKIP_SIZE    = 10;
    public static final int   DEFAULT_CURRENT_SKIP = 1;

    public static final int   MAX_PAGE_SIZE        = 100000;

    /**
     * 每页对象数
     */
    private int               pageSize;

    /**
     * 页数
     */
    private int               pageCount;

    /**
     * 对象数
     */
    private int               count;

    /**
     * 当前页
     */
    private int               currentPage;

    /**
     * 当前页开始的记录的位置
     */
    private int               begin;

    /**
     * 当前页结束的记录的位置
     */
    private int               end;

    /**
     * 快近页数量
     */
    private int               skipSize;

    /**
     * 当前快近所在页面
     */
    private int               currentSkip          = DEFAULT_CURRENT_SKIP;

    public Pagination(){
        this(DEFAULT_PAGE_SIZE, DEFAULT_CURRENT_PAGE, DEFAULT_SKIP_SIZE);
    }

    public Pagination(int pageSize, int currentPage){
        this(pageSize, currentPage, DEFAULT_SKIP_SIZE);
    }

    public Pagination(int pageSize, int currentPage, int skipSize){
        if (pageSize <= 0 || pageSize > MAX_PAGE_SIZE) pageSize = DEFAULT_PAGE_SIZE;
        if (currentPage <= 0) currentPage = DEFAULT_CURRENT_PAGE;
        if (skipSize <= 0) skipSize = DEFAULT_SKIP_SIZE;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.skipSize = skipSize;
    }

    public int getCurrentSkip() {
        return currentSkip;
    }

    public void setCurrentSkip(int currentSkip) {
        this.currentSkip = currentSkip;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setSkipSize(int skipSize) {
        this.skipSize = skipSize;
    }

    /**
     * 返回当前页
     *
     * @return 当前页
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * 得到页数
     *
     * @return 页数
     */
    public int getPageCount() {
        return pageCount;
    }

    /**
     * 得到每页记录条数
     *
     * @return 每页记录条数
     */
    public int getPageSize() {
        return this.pageSize;
    }

    /**
     * 得到记录条数
     *
     * @return 记录条数
     */
    public int getCount() {
        return count;
    }

    /**
     * 得到当前页的记录开始位置
     *
     * @return 开始位置
     */
    public int getBegin() {
        return begin;
    }

    /**
     * 得到记录的结束位置
     *
     * @return 结束位置
     */
    public int getEnd() {
        return end;
    }

    /**
     * 得到快近页数
     *
     * @return 快近页数
     */
    public int getSkipSize() {
        return this.skipSize;
    }

    /**
     * 设置记录条数
     */
    public void setCount(int count) {
        if (count <= 0) return;
        this.count = count;
        this.pageCount = count / pageSize + ((count % pageSize == 0) ? 0 : 1);
        if (currentPage > pageCount) currentPage = pageCount;
        if (currentPage <= 0) currentPage = 1;

        begin = (currentPage - 1) * pageSize;
        end = currentPage * pageSize;
        if (end >= count) end = count;

        currentSkip = (currentPage / skipSize) * skipSize + 1;
        if (currentPage % skipSize == 0) currentSkip = currentSkip - skipSize;
    }

    /**
     * 是否可以到第一页
     */
    public boolean canGoFirst() {
        return (this.currentPage > 1);
    }

    /**
     * 是否可以到第一页
     */
    public boolean isCanGoFirst() {
        return (this.currentPage > 1);
    }

    /**
     * 是否可以到前一页
     */
    public boolean isCanGoPrevious() {
        return (this.currentPage > 1);
    }

    /**
     * 是否可以到下一页
     */
    public boolean isCanGoNext() {
        return (this.currentPage < this.pageCount);
    }

    /**
     * 是否可以到最后一页
     */
    public boolean isCanGoLast() {
        // return currentPage!=pageCount&&pageCount!=0;
        return (this.currentPage < this.pageCount);
    }

    public boolean isLastPage() {
        return this.currentPage == this.pageCount;
    }

    /**
     * 得到前一页页码
     */
    public int previous() {
        if (this.currentPage > 1) return this.currentPage - 1;
        else return 1;
    }

    /**
     * 得到前一页页码
     */
    public int getPrevious() {
        if (this.currentPage > 1) return this.currentPage - 1;
        else return 1;
    }

    /**
     * 得到下一页页码
     */
    public int next() {
        if (this.currentPage < this.pageCount) return this.pageCount + 1;
        else return this.pageCount;
    }

    /**
     * 得到下一页页码
     */
    public int getNext() {
        if (this.currentPage < this.pageCount) return this.pageCount + 1;
        else return this.pageCount;
    }

    /**
     * 得到向前快近的页码
     */
    public int getSkipFrom() {
        if (this.getCurrentPage() - skipSize < 0) {
            return 1;
        }
        return this.getCurrentPage() - skipSize;
    }

    /**
     * 得到向后快近的页码
     */
    public int getSkipTo() {
        if (getSkipFrom() + skipSize > this.getPageCount()) {
            return this.getPageCount();
        }
        return getSkipFrom() + skipSize;
    }

    /**
     * 得到当前显示的页码
     */
    public int[] getCurrentSkipPageNumbers() {
        int count = skipSize;
        if (currentSkip + skipSize > pageCount) count = pageCount - currentSkip + 1;
        int[] Result = new int[count];
        for (int i = 0; i < count; i++) {
            Result[i] = currentSkip + i;
        }
        return Result;
    }

    /**
     * 得到当前显示的页码
     */
    public String getCurrentSkipPageNumbersString() {
        int count = skipSize;
        if (currentSkip + skipSize > pageCount) count = pageCount - currentSkip + 1;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < count; i++) {
            sb.append(currentSkip + i);
            sb.append(",");
        }
        String result = sb.toString();
        if (result != null && result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + currentPage;
        result = prime * result + pageSize;
        result = prime * result + skipSize;
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Pagination other = (Pagination) obj;
        if (currentPage != other.currentPage) return false;
        if (pageSize != other.pageSize) return false;
        if (skipSize != other.skipSize) return false;
        return true;
    }

}
