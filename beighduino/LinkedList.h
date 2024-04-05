#ifndef LinkedList_h
#define LinkedList_h

class LinkedList {
public:
    LinkedList() : head(nullptr), _size(0) {}

    ~LinkedList() {
        Node* current = head;
        while (current != nullptr) {
            Node* next = current->next;
            delete current;
            current = next;
        }
    }

    void add(void* data) {
        if (head == nullptr) {
            head = new Node(data);
        } else {
            Node* current = head;
            while (current->next != nullptr) {
                current = current->next;
            }
            current->next = new Node(data);
        }
        _size++;
    }

    bool remove(void* data) {
        Node* current = head;
        Node* previous = nullptr;

        while (current != nullptr) {
            if (current->data == data) {
                if (previous == nullptr) {
                    head = current->next;
                } else {
                    previous->next = current->next;
                }
                delete current;
                _size--;
                return true;
            }
            previous = current;
            current = current->next;
        }
        return false;
    }

    void* get(int index) {
        if (index < 0 || index >= _size) {
            return nullptr;  // Return nullptr for invalid index
        }
        Node* current = head;
        for (int i = 0; i < index; ++i) {
            current = current->next;
        }
        return current->data;
    }

    int size() {
        return _size;
    }

private:
    struct Node {
        void* data;
        Node* next;
        Node(void* d, Node* n = nullptr) : data(d), next(n) {}
    };

    Node* head;  // Pointer to the first node
    int _size;  // Size of the list
};

#endif