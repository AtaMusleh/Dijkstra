import java.util.*;

class XHeap<T> {
	private List<T> heap;
	private Comparator<T> compare;

	public XHeap(Comparator<T> compare) {
		this.heap = new ArrayList<>();
		this.compare = compare;
	}

	public void add(T element) {
		heap.add(element);
		heapifyUp(heap.size() - 1);
	}

	public T poll() {
		if (isEmpty()) {
			return null;
		}

		T root = heap.get(0);
		T lastElement = heap.remove(heap.size() - 1);

		if (!isEmpty()) {
			heap.set(0, lastElement);
			heapifyDown(0);
		}

		return root;
	}

	public boolean isEmpty() {
		return heap.isEmpty();
	}

	private void heapifyUp(int index) {
		while (index > 0) {
			int parentIndex = (index - 1) / 2;
			if (compare.compare(heap.get(index), heap.get(parentIndex)) < 0) {
				swap(index, parentIndex);
				index = parentIndex;
			} else {
				break;
			}
		}
	}

	private void heapifyDown(int index) {
		int leftChild = 2 * index + 1;
		int rightChild = 2 * index + 2;
		int smallest = index;

		if (leftChild < heap.size() && compare.compare(heap.get(leftChild), heap.get(smallest)) < 0) {
			smallest = leftChild;
		}

		if (rightChild < heap.size() && compare.compare(heap.get(rightChild), heap.get(smallest)) < 0) {
			smallest = rightChild;
		}

		if (smallest != index) {
			swap(index, smallest);
			heapifyDown(smallest);
		}
	}

	private void swap(int i, int j) {
		T temp = heap.get(i);
		heap.set(i, heap.get(j));
		heap.set(j, temp);
	}
}
