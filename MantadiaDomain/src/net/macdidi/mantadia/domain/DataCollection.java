package net.macdidi.mantadia.domain;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * 資料包裝類別
 * 
 * @author macdidi5
 *
 * @param <E> 包裝的元素型態
 */
@Root
public class DataCollection<E> {
    
    @ElementList(inline=true, required=false)
    private List<E> list = new ArrayList<E>();
    
    /**
     * 新增元素
     * 
     * @param item 新增的元素物件
     */
    public void add(E item) {
        list.add(item);
    }
    
    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }

}
