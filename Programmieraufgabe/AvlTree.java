package ads1ss12.pa;

import java.util.ArrayList;
import java.util.List;

/**
 * AVL-Baum-Klasse die die fehlenden Methoden aus {@link AbstractAvlTree}
 * implementiert.
 * 
 * <p>
 * In dieser Klasse m&uuml;ssen Sie Ihren Code einf&uuml;gen und die Methoden
 * {@link #remove remove()} sowie {@link #rotateLeft rotateLeft()} und
 * {@link #rotateRight rotateRight()} implementieren.
 * </p>
 * 
 * <p>
 * Sie k&ouml;nnen beliebige neue Variablen und Methoden in dieser Klasse
 * hinzuf&uuml;gen. Wichtig ist nur, dass die oben genannten Methoden
 * implementiert werden.
 * </p>
 * 
 * @author Manuel Geier (1126137)
 */
public class AvlTree extends AbstractAvlTree {
	
	/**
	 * Der Default-Konstruktor.
	 * 
	 * Ruft einfach nur den Konstruktor der Oberklasse auf.
	 */
	public AvlTree() {
		super();
	}

	/**
	 * F&uuml;gt ein Element mit dem Schl&uuml;ssel <code>k</code> ein.
	 * 
	 * <p>
	 * Existiert im AVL-Baum ein Knoten mit Schl&uuml;ssel <code>k</code>, soll
	 * <code>insert()</code> einfach nichts machen.
	 * </p>
	 * 
	 * <p>
	 * Nach dem Einf&uuml;gen muss sichergestellt sein, dass es sich bei dem
	 * resultierenden Baum immer noch um einen AVL-Baum handelt, und dass
	 * {@link AbstractAvlTree#root root} auf die tats&auml;chliche Wurzel des
	 * Baums zeigt!
	 * </p>
	 * 
	 * @param k
	 *            Der Schl&uuml;ssel der eingef&uuml;gt werden soll.
	 * @see AbstractAvlTree#insert
	 */
	public void insert(int k) {
		if(root == null) {
			root = new AvlNode(k);
		} else {
			
			insert(root, new AvlNode(k));
			
		}
	}
	
	/**
	 * F&uuml;gt ein Element mit dem Schl&uuml;ssel <code>k</code> ein.
	 * 
	 * @param node
	 *            Knoten unter dem der neue Knoten eingefuegt werden soll.
	 * @param newNode
	 * 			  Der Knoten der eingef&uuml;gt werden soll.
	 */
	private void insert(AvlNode node, AvlNode newNode) {
		if(newNode.key < node.key) {
		// Key gehoert nach links
			
			if(node.left != null) {
			// Links weiter nach unten gehen
				
				insert(node.left, newNode);
				rebalance(node);
				
			} else {
			// Links einfuegen
				
				node.left = newNode;
				newNode.parent = node;
			}
		} else if(newNode.key > node.key){
		// Key gehoert nach rechts
			
			if(node.right != null) {
			// Rechts weiter nach unten gehen
				
				insert(node.right, newNode);
				rebalance(node);
				
			} else {
			// Rechts einfuegen
				
				node.right = newNode;
				newNode.parent = node;
			}
		} else {
			// Bereits vorhanden
		}
	}

	
	/**
	 * Entfernt den Knoten mit Schl&uuml;ssel <code>k</code> falls er existiert.
	 * 
	 * <p>
	 * Existiert im AVL-Baum kein Knoten mit Schl&uuml;ssel <code>k</code>, soll
	 * <code>remove()</code> einfach nichts machen.
	 * </p>
	 * 
	 * <p>
	 * Nach dem Entfernen muss sichergestellt sein, dass es sich bei dem
	 * resultierenden Baum immer noch um einen AVL-Baum handelt, und dass
	 * {@link AbstractAvlTree#root root} auf die tats&auml;chliche Wurzel des
	 * Baums zeigt!
	 * </p>
	 * 
	 * @param k
	 *            Der Schl&uuml;ssel dessen Knoten entfernt werden soll.
	 * 
	 * @see AbstractAvlTree#root
	 * @see #rotateLeft rotateLeft()
	 * @see #rotateRight rotateRight()
	 */
	public void remove(int k) {
		
		if(root == null) {
			// Nichts zu loeschen
		} else {
			
			remove(root, k);
			
		}
		
	}
	
	/**
	 * Entfernt den Knoten mit Schl&uuml;ssel <code>k</code> falls er existiert.
	 * 
	 * @param node
	 * 			  Knoten von dem ausgegangen wird.
	 * @param k
	 *            Der Schl&uuml;ssel dessen Knoten entfernt werden soll.
	 */
	private void remove(AvlNode node, int k) {
		if(node != null) {
			if(node.key > k) {
			// Key links suchen
				
				remove(node.left, k);
				
			} else if(node.key < k) {
			// Key rechts suchen
				
				remove(node.right, k);
				
			} else {
			// Key gefunden
				
				
				AvlNode successor = null;
				AvlNode p;
				
				if(node.left == null || node.right == null) {
					successor = node;
				} else {
					
					// Successor
					successor = getSuccessor(node);
					
					node.key = successor.key;
					node.balance = successor.balance;
				}
				
				if(successor.left != null) {
					p = successor.left;
				} else {
					p = successor.right;
				}
				
				if(p != null) {
					p.parent = successor.parent;
				}
				
				if(successor.parent == null) {
					root = p;
				} else {
					if(successor.parent.left != null && successor.key == successor.parent.left.key) {
						successor.parent.left = p;
					} else {
						successor.parent.right = p;
					}
				}
				
				successor.balance = calculateBalance(successor);
				if(successor.left != null) successor.left.balance = calculateBalance(successor.left);
				if(successor.right != null) successor.right.balance = calculateBalance(successor.right);
				
				rebalanceToRoot(successor.parent);
			}
			
		}
	}
	

	/**
	 * Berechnet die Balance eines Knotens.
	 * 
	 * @param node
	 * 			Knoten der zu berechnen ist
	 * @return Balance-Wert des Knotens
	 */
	public int calculateBalance(AvlNode node) {
		return height(node.right) - height(node.left);
	}

	/**
	 * Berechnet die Hoehe eines Knotens.
	 * 
	 * @param node
	 * 			Knoten der zu berechnen ist
	 * @return Hoehen-Wert des Knotens
	 */
	public int height(AvlNode node) {
		if(node == null) {
			return 0;
		}
		
		return 1 + Math.max(height(node.left), height(node.right));
	}
	
	/**
	 * Rebalanciert einen Knoten bis hoch zum Root-Knoten.
	 * 
	 * @param node 
	 * 			Knoten der rebalanciert werden soll
	 */
	private void rebalanceToRoot(AvlNode node) {
		if(node == null)
			return;
		
		AvlNode oldParent = node.parent;
		rebalance(node);
		rebalanceToRoot(oldParent);
	}
	
	/**
	 * Rebalanciert einen Knoten
	 * 
	 * @param node
	 * 			Knoten der rebalanciert werden soll
	 */
	private void rebalance(AvlNode node) {
		
		int balance = calculateBalance(node);
		node.balance = balance;
	
		// Balance pruefen
		if(balance == -2) {
			
			if(node.left != null && height(node.left.left) >= height(node.left.right)) {
				
				// Fall 1.1
				rotateRight(node);
				
			} else {
				
				// Fall 1.2
				rotateLeft(node.left);
				rotateRight(node);

			}
			
		} else if(balance == 2) {
			
			if(node.right != null && height(node.right.right) >= height(node.right.left)) {
				
				// Fall 2.1
				rotateLeft(node);
				
			} else {
				
				// Fall 2.2
				rotateRight(node.right);
				rotateLeft(node);
				
			}
			
		}
		
	}
	
	/**
	 * Liefert den Nachfolger des Knotens.
	 * 
	 * @param node
	 * 			Knoten fuer den der Nachfolger gesucht wird.
	 * @return Nachfolger des Knotens
	 */
	public AvlNode getSuccessor(AvlNode node) {
		if(node.right != null) {
			return getMinimum(node.right);
		} else {
			AvlNode successorNode = node.parent;
			
			while(successorNode != null && node == successorNode.right) {
				node = successorNode;
				successorNode = successorNode.parent;
			}
			
			return successorNode;
		}
	}
	
	/**
	 * Liefert den Knoten mit dem Kleinsten Key ausgehend, vom uebergebenen Knoten zurueck.
	 * 
	 * @param node
	 * 			Knoten von dem aus der kleineste Key gesucht wird
	 * @return Kleinster Knoten
	 */
	public AvlNode getMinimum(AvlNode node) {
		if(node == null) {
			return null;
		}
		
		while(node.left != null) {
			node = node.left;
		}
		return node;
	}
	
	/**
	 * Liefert den Knoten mit dem groessten Key ausgehend, vom uebergebenen Knoten zurueck.
	 * 
	 * @param node
	 * 			Knoten von dem aus der groesste Key gesucht wird
	 * @return Groesster Knoten
	 */
	public AvlNode getMaximum(AvlNode node) {
		if(node == null) {
			return null;
		}
		
		while(node.right != null) {
			node = node.right;
		}
		return node;
	}

	/**
	 * F&uuml;hrt eine Links-Rotation beim Knoten <code>n</code> durch.
	 * 
	 * 
	 * @param n
	 *            Der Knoten bei dem die Rotation durchgef&uuml;hrt werden soll.
	 * 
	 * @return Die <em>neue</em> Wurzel des rotierten Teilbaums.
	 */
	public AvlNode rotateLeft(AvlNode n) {
		AvlNode newRoot = n.right;
		AvlNode oldRoot = n;
		
		newRoot.parent = oldRoot.parent;
		if(oldRoot.parent == null) {
			root = newRoot;
		} else {
			if(oldRoot.parent.left != null && oldRoot.parent.left == oldRoot)
				oldRoot.parent.left = newRoot;
			else
				oldRoot.parent.right = newRoot;
		}
		
		oldRoot.right = newRoot.left;
		if(oldRoot.right != null)
			oldRoot.right.parent = oldRoot;
		
		newRoot.left = oldRoot;
		oldRoot.parent = newRoot;
		
//		oldRoot.balance = 1 + Math.max(newRoot.balance, 0);
//		newRoot.balance = 1 - Math.min(oldRoot.balance, 0);
		
		newRoot.balance = calculateBalance(newRoot);
		if(newRoot.left != null) newRoot.left.balance = calculateBalance(newRoot.left);
		if(newRoot.right != null) newRoot.right.balance = calculateBalance(newRoot.right);
				
		return newRoot;
	}

	/**
	 * F&uuml;hrt eine Rechts-Rotation beim Knoten <code>n</code> durch.
	 * 
	 * 
	 * @param n
	 *            Der Knoten bei dem die Rotation durchgef&uuml;hrt werden soll.
	 * 
	 * @return Die <em>neue</em> Wurzel des rotierten Teilbaums.
	 */
	public AvlNode rotateRight(AvlNode n) {
		AvlNode newRoot = n.left;
		AvlNode oldRoot = n;
		
		newRoot.parent = oldRoot.parent;
		if(oldRoot.parent == null) {
			root = newRoot;
		} else {
			if(oldRoot.parent.left != null && oldRoot.parent.left == oldRoot)
				oldRoot.parent.left = newRoot;
			else
				oldRoot.parent.right = newRoot;
		}
		
		oldRoot.left = newRoot.right;
		if(oldRoot.left != null)
			oldRoot.left.parent = oldRoot;
		
		newRoot.right = oldRoot;
		oldRoot.parent = newRoot;
		
//		oldRoot.balance = 1 - Math.max(newRoot.balance, 0);
//		newRoot.balance = 1 + Math.min(oldRoot.balance, 0);
		
		newRoot.balance = calculateBalance(newRoot);
		if(newRoot.left != null) newRoot.left.balance = calculateBalance(newRoot.left);
		if(newRoot.right != null) newRoot.right.balance = calculateBalance(newRoot.right);
				
		return newRoot;
	}
	
	/**
	 * Liefert eine String-Darstellung des Baumes.
	 * 
	 * @return String Baum
	 */
	@Override
	public String toString() {
		String s = "\n";
		
		List<List<String>> l = new ArrayList<List<String>>();
		toString(root, 0, l);
		for(int i=0; i<l.size(); i++) {
			
			List<String> a =  l.get(i);
			
			for(int j=0; j<a.size(); j++) {
				
				String b = a.get(j);

				for(int k=0; k<l.size()-i; k++)
					s += "    ";
				
				s += b;
				
			}
			s += "\n";
		}
		
		return s;
	}
	
	/**
	 * Liefert eine String-Darstellung des Baumes. 
	 * 
	 * @param n
	 * 			aktueller Knoten
	 * @param level
	 * 			aktuelles Level/Tiefe
	 * @return String des Baumes
	 */
	public String toString(AvlNode n, int level) {
		String s = "";
		if(n != null) {
			for(int i=0; i<level; i++)
				s+="|";
			s += "+-- " + n.toString() + "\n";
			s += toString(n.left, level+1);
			s += toString(n.right, level+1);
		}
		return s;
	}
	
	/**
	 * Liefert eine List-String-Darstellung des Baumes. 
	 * 
	 * @param n
	 * 			aktueller Knoten
	 * @param level
	 * 			aktuelles Level/Tiefe
	 * @param strTree
	 * 			Liste in der alle Knoten gespeichert werden
	 */
	public void toString(AvlNode n, int level, List<List<String>> strTree) {
		
		try {
			strTree.get(level);
		} catch(IndexOutOfBoundsException e) {
			strTree.add(new ArrayList<String>());
		}
		
		if(n == null) {
			
			strTree.get(level).add(null);
			
		} else {
//			strTree.get(level).add(String.format("%4s", n.toString()));
			strTree.get(level).add(String.format("%4s (%s)", n.toString(), n.balance));
			
			toString(n.left, level+1, strTree);
			toString(n.right, level+1, strTree);
		}
		
	}
	
	/**
	 * Prueft die Parent-Kind-Parent-Beziehung des Baumes.
	 * 
	 * @return true wenn alle Parent-Beziehung ok sind, andernfalls false
	 */
	public boolean isParentCheckOk() {
		return checkParents(root);
	}
	
	/**
	 * Prueft die Parent-Kind-Parent-Beziehung eines bestimmten Knotens.
	 * 
	 * @param node
	 * 			Knoten der zu pruefen ist
	 * @return true wenn die Parent-Beziehung ok ist, andernfalls false
	 */
	private boolean checkParents(AvlNode node) {
		boolean flag = true;
		
		if(node.left != null){
			if(node.left.parent.key != node.key)
				flag = false;
			if(!checkParents(node.left))
				flag = false;
		}
		
		if(node.right != null) {
			if(node.right.parent.key != node.key)
				flag = false;
			if(!checkParents(node.right))
				flag = false;
		}
		
		return flag;
	}
	
	/**
	 * Prueft die Balance des Baumes.
	 * 
	 * @return Liefert true wenn der Baum komplett ausbalanciert ist. Andernfalls false.
	 */
	public boolean isBalanceCheckOk() {
		return checkBalance(root);
	}
	
	/**
	 * Prueft die Balance eines bestimmten Knotens.
	 * 
	 * @param node
	 * 			Knoten der zu pruefen ist
	 * @return true wenn die Balance ok ist, andernfalls false
	 */
	private boolean checkBalance(AvlNode node) {
		boolean flag = true;
		
		int hl = height(node.left);
		int hr = height(node.right);
		int balance = hr - hl;
		if(balance>=2 || balance<=-2)  {
			System.out.println("BAL ERR: " + node.key);
			System.out.println("hl: "+hl+"  hr: "+hr+"  h:" + balance);
			return false;
		}
		
		if(balance != node.balance) {
			System.out.println("ist: " + node.balance + " soll: " + balance);
			return false;
		}
		
		if(node.left != null){
			if(!checkBalance(node.left))
				flag = false;
		}
		
		if(node.right != null) {
			if(!checkBalance(node.right))
				flag = false;
		}
		
		return flag;
	}
	

}