package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {
	
	final static WikiFetcher wf = new WikiFetcher();

	private static String findLink(Iterable<Node> iter, String curr, LinkedList<String> visited) {
		int paren = 0;
		for(Node n: iter) {

			if(n instanceof TextNode) {
				if(n.toString().contains("(")) {
					paren++;
				}
				if(n.toString().contains(")")) {
					paren--;
				}
			}
			if(paren != 0) {
				continue;
			}

			if(n instanceof Element) {
				Element currNode = (Element) n;
			//	String url = n.attr("href");
				if(currNode.tagName().equals("a")) {
					// check for italics
					boolean hasItalics = false;
					for(Element e: currNode.parents()) {
						if(e.tagName().equals("i") || e.tagName().equals("em")) {
							hasItalics = true;
						}
					}
					if(hasItalics) continue;
					if(Character.isUpperCase(currNode.text().charAt(0))) continue;
					// get url
					String url = currNode.attr("href");
					// skip if non-wiki entry (external link)
					if(!url.toLowerCase().startsWith("/wiki")) continue;
					// skip if url points to current page
					if(("https://en.wikipedia.org/wiki"+url).equals(curr)) continue;

					if(visited.contains(url)) continue;

					visited.add(url);
					return "https://en.wikipedia.org" + url;


				}
				// skip italics
				// skip uppercase
				// skip external links?
				// skip links to curr page


			}
		}
		return ""; // else return empty string

	}
	public static boolean recursiveDFS(String url, LinkedList<String> visited) throws IOException {
		System.out.println(url);
		Elements paragraphs = wf.fetchWikipedia(url);
		Element firstPara = paragraphs.get(0);
		Iterable<Node> iter = new WikiNodeIterable(firstPara);
		String f = findLink(iter, url, visited);
		if(f.equals("")) {
			System.out.println("empty string");
			return false;
		}
		if(f.equals("https://en.wikipedia.org/wiki/Philosophy")) {
			return true;
		}
		return recursiveDFS(f, visited);
	}

	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 * 
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 * 
	 * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// some example code to get you started
		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		Elements paragraphs = wf.fetchWikipedia(url);
		Element firstPara = paragraphs.get(0);
		Iterable<Node> iter = new WikiNodeIterable(firstPara);
		LinkedList<String> visited = new LinkedList<String>();

		System.out.println(recursiveDFS(url, visited));

		// if page has no links or if first link has already been seen
		//if(list.contains(node)) {
		//	return False;
		//}


        // the following throws an exception so the test fails
        // until you update the code
    //    String msg = "Complete this lab by adding your code and removing this statement.";
    //    throw new UnsupportedOperationException(msg);
	}
}

/*

				System.out.println("URL: " + url);

				Element parent = currNode.parent();
				// ignore already visited or if empty url or if url == current page
				//&& !url.toLowerCase().startsWith("/wiki")
				if(!visited.contains(url) && url.toLowerCase().startsWith("/wiki") && !url.isEmpty() && !("https://en.wikipedia.org/wiki"+url).equals(curr)) {
					// ignore italics
					if(!parent.hasAttr("a") && !parent.hasAttr("em")) {
						visited.add(url);
						return "https://en.wikipedia.org" + url;
					}
				}
 */