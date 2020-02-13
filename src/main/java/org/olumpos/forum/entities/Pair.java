package org.olumpos.forum.entities;

/************************************************************************************************************************
 * 
 * @author daristote
 *
 * Classe qui permet de retourner une paire d'un Topic et d'un Post lors de l'ajout d'un nouveau Topic
 * On paramétrise la classe avec deux types génériques, ce qui la rend réutilisable pour d'autres types d'objets que Topic et Post
 * 
 * @param <T> : type du champ first
 * @param <P> : type du champ second
 * 
 ************************************************************************************************************************/


public class Pair<T, P> {

	private T first;
	private P second;

	public Pair(T first, P second) {
		this.first = first;
		this.second = second;
	}

	public final T getFirst() {
		return first;
	}

	public final void setFirst(T first) {
		this.first = first;
	}

	public final P getSecond() {
		return second;
	}

	public final void setSecond(P second) {
		this.second = second;
	}

	@Override
	public String toString() {
		return "Pair: First: " + first + "\nSecond " + second;
	}

}
