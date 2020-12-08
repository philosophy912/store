package com.sophia.store.utils;


/**
 * @author lizhe
 * @date 2020/11/26 11:08
 **/
public final class PageUtils {

    // totalPages
    public static int get(long total, int pageSize) {
        int count = (int) total;
        if (pageSize <= 0) {
            String error = "page size must more than 0";
            throw new RuntimeException(error);
        }
        if (count < 0) {
            String error = "count must more than 0";
            throw new RuntimeException(error);
        }
        if (count == 0) {
            return 0;
        } else {
            if (count % pageSize == 0) {
                return count / pageSize;
            } else {
                return count / pageSize + 1;
            }
        }
    }


}
