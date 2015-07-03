/*****************************************************************
 * JADE - Java Agent DEvelopment Framework is a framework to develop
 * multi-agent systems in compliance with the FIPA specifications.
 * Copyright (C) 2000 CSELT S.p.A.
 * <p>
 * GNU Lesser General Public License
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 *****************************************************************/

package jade.util.leap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Vector;

/**
 The LEAP (environment-dependent) version of the &qote;java.util.ArrayList&qote; class.
 This class appears to be exactly the same in J2SE, PJAVA and MIDP.
 The internal implementation is different in the three cases however.

 @author Nicolas Lhuillier
 @version 1.0, 29/09/00

 @see java.util.ArrayList
 */
public class ArrayList<X> implements List<X>, Serializable {
    private transient java.util.List<X> realHiddenList = null;
    private Vector hiddenList;

    // This is needed to ensure compatibility with the J2ME version of 
    // this class in serialization/deserialization operations
    private static final long serialVersionUID = 3487495895819393L;

    /**
     * Default Constructor, creates an empty List
     */
    public ArrayList() {
        realHiddenList = new java.util.ArrayList();
    }

    /**
     * Constructor specifying list size
     */
    public ArrayList(int size) {
        realHiddenList = new java.util.ArrayList(size);
    }

    /**
     * Constructs an ArrayList from a java.util.ArrayList.
     */
    public ArrayList(java.util.ArrayList<X> toBeHiddenList) {
        realHiddenList = toBeHiddenList;
    }

    /**
     * @see List interface
     */
    public void clear() {
        realHiddenList.clear();
    }

    /**
     * @see List interface
     */
    public boolean contains(Object o) {
        return realHiddenList.contains(o);
    }

    /**
     * @see List interface
     */
    public X get(int index) {
        return realHiddenList.get(index);
    }

    /**
     * @see List interface
     */
    public int indexOf(X o) {
        return realHiddenList.indexOf(o);
    }

    /**
     * @see List interface
     */
    public X remove(int index) {
        return realHiddenList.remove(index);
    }

    /**
     Inserts the specified element at the specified position in this list
     */
    public void add(int index, X o) {
        realHiddenList.add(index, o);
    }

    /**
     * @see Collection interface
     */
    public boolean add(X o) {
        return realHiddenList.add(o);
    }

    /**
     * @see Collection interface
     */
    public boolean isEmpty() {
        return realHiddenList.isEmpty();
    }

    /**
     * @see Collection interface
     */
    public boolean remove(X o) {
        return realHiddenList.remove(o);
    }

    public String toString() {
        return realHiddenList.toString();
    }

    /**
     * @see Collection interface
     */
    public Iterator iterator() {
        return new Iterator() {
            java.util.Iterator<X> it = ArrayList.this.realHiddenList.iterator();

            /**
             * Method declaration
             *
             * @return
             *
             */
            public boolean hasNext() {
                return it.hasNext();
            }

            /**
             * Method declaration
             *
             * @return
             *
             */
            public X next() {
                return it.next();
            }

            /**
             * Method declaration
             *
             */
            public void remove() {
                it.remove();
            }

        };
    }

    /**
     * @see Collection interface
     */
    public Object[] toArray() {
        return realHiddenList.toArray();
    }

    /**
     * @see Collection interface
     */
    public int size() {
        return realHiddenList.size();
    }

    /**
     * Method declaration
     *
     * @return java.lang.Object
     *
     */
    public ArrayList<X> clone() {
        ArrayList<X> result = new ArrayList();
        result.fromList(realHiddenList);
        return result;
    }

    /**
     * Method declaration
     *
     * @return java.util.List
     *
     */
    public java.util.List<X> toList() {
        return realHiddenList;
    }

    /**
     Fill this list with the content of a given
     <code>java.util.List</code> object.
     @param l The <code>java.util.List</code> to copy the content
     from.
     */
    public void fromList(java.util.List<X> l) {
        clear();
        realHiddenList.addAll(l);
    }

    /**
     A customized writeObject() method is needed to ensure
     compatibility with the J2ME version of this class in
     serialization/deserialization operations.
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        hiddenList = new Vector();

        java.util.Iterator it = realHiddenList.iterator();

        while (it.hasNext()) {
            hiddenList.add(it.next());
        }

        out.defaultWriteObject();
    }

    /**
     A customized readObject() method is needed to ensure compatibility with
     the J2ME version of this class in serialization/deserialization
     operations
     */
    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        realHiddenList = new java.util.ArrayList();

        in.defaultReadObject();

        Enumeration<X> e = hiddenList.elements();

        while (e.hasMoreElements()) {
            realHiddenList.add(e.nextElement());
        }
    }

    // For persistence service
    private void setData(java.util.List data) {
        realHiddenList = data;
    }

    // For persistence service
    private java.util.List getData() {
        return realHiddenList;
    }

}

