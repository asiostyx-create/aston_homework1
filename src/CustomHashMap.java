import java.util.Objects;

public class CustomHashMap<K, V> {
    private class Entry<K, V> {
        final K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private static final int CAPACITY = 16;
    private Entry<K, V>[] buckets;

    CustomHashMap() {
        buckets = new Entry[CAPACITY];
    }

    private int hash(K key) {
        if (key != null) {
            int h = key.hashCode();
            return (h ^ (h >>> 16)) & (buckets.length - 1);
        } else {
            return 0;
        }
    }

    public void put(K key, V value) {
        int index = hash(key);
        Entry<K, V> entry = new Entry<>(key, value);

        if (buckets[index] == null) {
            buckets[index] = entry;
        } else {
            Entry<K, V> current = buckets[index];
            do {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    break;
                }
                current = current.next;
            } while (true);
            current.next = entry;
        }
    }

    public V get(K key) {
        int index = hash(key);
        Entry<K, V> current = buckets[index];

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            } else {
                current = current.next;
            }
        }
        return null;
    }

    public V remove(K key) {
        int index = hash(key);
        Entry<K, V> current = buckets[index];
        Entry<K, V> prev = null;

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                if (prev == null) {
                    buckets[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                return current.value;
            }
            prev = current;
            current = current.next;
        }
        return null;
    }
}
