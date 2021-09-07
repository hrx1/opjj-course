package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Class provides mechanism for storing sorted set of unique numbers
 * 
 * @author Hrvoje
 *
 */
public class UniqueNumbers {

	/**
	 * Takes integers from standard input and sorts them in ascending and descending
	 * order. Stops taking integers when string "kraj" is read.
	 * 
	 * @param args
	 *            not in use
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		TreeNode head = null;

		String inputLine;
		int inputNumber;
		while (true) {
			System.out.print("Unesite broj > ");
			inputLine = sc.nextLine();

			try { // pokusaj parsirat broj i usput provjeri granice
				inputNumber = Integer.parseInt(inputLine);
			} catch (NumberFormatException ex) {
				if (inputLine.equals("kraj")) {
					break;
				} else {
					System.out.println("'" + inputLine + "' nije cijeli broj.");
					continue;
				}
			}

			if (containsValue(head, inputNumber)) {
				System.out.println("Broj vec postoji. Preskacem.");
			} else {
				head = addNode(head, inputNumber);
				System.out.println("Dodano.");
			}
		}

		sc.close();

		System.out.print("Ispis od najmanjeg do najveceg: ");
		printAscending(head);
		System.out.println();
		System.out.print("Ispis od najveceg do najmanjeg: ");
		printDescending(head);
		System.out.println();
	}

	/**
	 * Constructor for TreeNode which holds value and points to left and right
	 * TreeNode.
	 * 
	 * @author john
	 *
	 */
	public static class TreeNode {
		int value;
		TreeNode left, right;

		public TreeNode(int value) {
			this.value = value;
			left = right = null;
		}
	}

	/**
	 * Adds a node with a value in a Tree
	 * 
	 * @param head
	 *            of the tree
	 * @param value
	 *            to be added
	 * @return head of the tree
	 */
	public static TreeNode addNode(TreeNode head, int value) {
		TreeNode newNode = new TreeNode(value);

		if (head == null) {
			return newNode;
		}

		TreeNode currentNode = head;
		while (true) {
			if (currentNode.value > value) {
				if (currentNode.left == null) {
					currentNode.left = newNode;
					break;
				} else {
					currentNode = currentNode.left;
				}
			} else if (currentNode.value < value) {
				if (currentNode.right == null) {
					currentNode.right = newNode;
					break;
				} else {
					currentNode = currentNode.right;
				}
			} else { // ako je isti, nemoj ga dodat
				break;
			}
		}

		return head;
	}

	/**
	 * Calculates size of a tree
	 * 
	 * @param head
	 * @return size of a tree
	 */
	public static int treeSize(TreeNode head) {
		if (head == null) {
			return 0;
		} else {
			return 1 + treeSize(head.left) + treeSize(head.right);
		}
	}

	/**
	 * Checks whether Tree with node head contains Node with value
	 * 
	 * @param head
	 *            head of a Tree
	 * @param value
	 * @return true if contains
	 */
	public static boolean containsValue(TreeNode head, int value) {
		if (head == null) {
			return false;
		} else if (head.value == value) {
			return true;
		} else if (head.value > value) {
			return containsValue(head.left, value);
		} else {
			return containsValue(head.right, value);
		}
	}

	/**
	 * Prints Tree values in ascending order
	 * 
	 * @param head
	 *            of a Tree
	 */
	public static void printAscending(TreeNode head) {
		if (head == null) {
			return;
		}
		printAscending(head.left);
		System.out.print(head.value + " ");
		printAscending(head.right);
	}

	/**
	 * Prints Tree values in descending order
	 * 
	 * @param head
	 *            of a Tree
	 */
	public static void printDescending(TreeNode head) {
		if (head == null) {
			return;
		}
		printDescending(head.right);
		System.out.print(head.value + " ");
		printDescending(head.left);
	}
}
