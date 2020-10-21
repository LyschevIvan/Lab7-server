


package com.company.Application.Controllers;


import com.company.Application.ProductClasses.Product;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * uses to manage collection
 */
@XmlRootElement
public class TreeMapController
{

    private TreeMap<Integer, Product> products = new TreeMap<>();

    /**
     * put Product to Map by key
     * @param key Integer
     * @param product product
     */
    public void put(Integer key, Product product){
        products.put(key,product);
    }

    /**
     * returns map of products
     * @return TreeMap
     */
    @XmlElement
    public TreeMap<Integer, Product> getProducts() {
        return products;
    }

    /**
     * set Map of products
     * @param products TreeMap
     */
    void setProducts(TreeMap<Integer, Product> products) {
        this.products = products;
    }

    /**
     * removes element by key
     * @param k Integer
     */
    public void remove(Integer k) {
        products.remove(k);

    }

    /**
     * shows info about collection file
     */
    public String getInfo(DbControllerInterface dbController){
        SimpleDateFormat date_format = new SimpleDateFormat("HH:mm:ss dd.MM.yy");

        BasicFileAttributes attributes = dbController.getAttrs();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Тип: TreeMap").append('\n');
        stringBuilder.append("Количество элементов: ").append(products.size()).append('\n');
        stringBuilder.append("Дата создания: ").append(attributes == null ? "файл еще не создавался" : date_format.format(attributes.creationTime().toMillis())).append('\n');
        stringBuilder.append("Дата последнего изменения: ").append(attributes == null ? "файл еще не создавался" : date_format.format(attributes.lastModifiedTime().toMillis()));
        return stringBuilder.toString();
    }

    /**
     * clears collection
     */
    public void clear(){
        products.clear();
    }

    /**
     * returns key iterator
     * @return Iterator
     */
    public Iterator<Integer> getKeyIterator(){
        return products.keySet().iterator();
    }

    /**
     * returns value iterator
     * @return Iterator
     */
    public Iterator<Product> getValueIterator(){
        return products.values().iterator();
    }



    /**
     * returns list of Id
     * @return LinkedList
     */
    public LinkedList<Long> getIdList(){
        LinkedList<Long> idList = new LinkedList<>();
        for(Product value : products.values())
            idList.add(value.getId());
        return idList;
    }


}
