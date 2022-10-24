import jdk.swing.interop.SwingInterOpUtils;

import java.util.Objects;

public class LinkedList<T extends DualArgument<T>> {
    Element<T> head; //First Element (Head)
    Element<T> tail; //Last Element

    public Element<T> getHead() {
        return head;
    }

    public Element<T> getTail() {
        Element current = head;
        if (current == null)
            return null;
        else {
            while (current.next != null) {
                current = current.next;
            }
            return current;
        }
    }

    //returns the largest Element smaller than compareTo
    public Element<T> prev(Element<T> compareTo) {
        Element<T> current = head;
        if (current == null)
            return null;
        if (current.compareTo(compareTo) > 0)
            return null;
        if (current.equals(tail)) {
            return current.compareTo(compareTo) < 0 ? current : null;
        }

        //Now we have at least two Element in the list
        Element<T> next = current.next;
        while (next != null && next.compareTo(compareTo) < 0) {
            current = next;
            next = next.next;
        }
        return current;
    }

    public void insert(T toInsert) {
        Element<T> newElement = new Element<T>(toInsert);
        if (head == null) {//no element in the list
            head = newElement;
            tail = head;

        } else if (head.equals(tail)) {
            if (head.compareTo(newElement) < 0) {
                head.next = newElement;
                tail = newElement;
            } else {
                newElement.next = head;
                head = newElement;
            }
        } else if (head.compareTo(newElement) > 0) {
            newElement.next = head;
            head = newElement;
        } else {
            //Now we have at least two Elements in the list
            Element<T> current = head;
            //We check wether the newElement should go in between the current and its successor
            //current.next!=null assures we can compare it to the next Element, if not we now we are at the tail
            while (current.next != null && current.next.compareTo(newElement) < 0) {
                if (current.next.compareTo(newElement) == 0)
                    break;
                current = current.next;
            }
            if (current.equals(tail)) {
                tail = newElement;
            }
            newElement.next = current.next;
            current.next = newElement;

        }
    }

    //We assume the Element to delete is in the List since we only want to remove such Elements
    //in the Algorithm
    public void delete(Element<T> toDelete) {
        if (head.equals(toDelete)) {
            if (head.equals(tail)) {
                head = tail = null;
                return;
            }
            head = head.next;
            return;
        }
        Element<T> prev = head;
        Element<T> current = head.next;
        while (!current.equals(toDelete)) {
            prev = prev.next;
            current = current.next;
        }
        //Now we have the Element to Delete, we do so, by removing the reference in prev to it
        if (current.equals(tail)) {
            tail = prev;
        }
        prev.next = current.next;

    }


    //For visualising purposes
    public String toString() {
        String output = "";

        Element<T> current = head;
        while (current != null) {
            output += " " + current;
            current = current.next;
        }

        return output;
    }


}

interface DualArgument<T> extends Comparable<T> {
    int compareToSecond(T o);
}

class Element<T extends DualArgument<T>> implements DualArgument<Element<T>> {
    T content;
    Element<T> next;

    public Element(T content) {
        this.content = content;
        next = null;
    }


    public void setNext(T next) {
        this.next = new Element<T>(next);
    }


    @Override
    public int compareTo(Element<T> o) {
        return content.compareTo(o.content);
    }

    public String toString() {
        return content.toString();
    }

    @Override
    public int compareToSecond(Element<T> o) {
        return content.compareToSecond(o.content);
    }
}

class Tuple<T extends Comparable<T>> implements DualArgument<Tuple<T>> {
    T first;
    T second;

    public Tuple(T first, T second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int compareTo(Tuple<T> o) {
        if (first.compareTo(o.first) != 0)
            return first.compareTo(o.first);
        else
            return second.compareTo(o.second);
    }

    public String toString() {
        return "(" + first.toString() + "," + second.toString() + ")";

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?> tuple = (Tuple<?>) o;
        return Objects.equals(first, tuple.first) && Objects.equals(second, tuple.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public int compareToSecond(Tuple<T> o) {
        return -second.compareTo(o.second);
    }
}
