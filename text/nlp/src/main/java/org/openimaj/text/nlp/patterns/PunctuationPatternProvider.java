/**
 * Copyright (c) 2011, The University of Southampton and the individual contributors.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *   * 	Redistributions of source code must retain the above copyright notice,
 * 	this list of conditions and the following disclaimer.
 *
 *   *	Redistributions in binary form must reproduce the above copyright notice,
 * 	this list of conditions and the following disclaimer in the documentation
 * 	and/or other materials provided with the distribution.
 *
 *   *	Neither the name of the University of Southampton nor the names of its
 * 	contributors may be used to endorse or promote products derived from this
 * 	software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.openimaj.text.nlp.patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openimaj.text.util.RegexUtil;

public class PunctuationPatternProvider extends PatternProvider{
	
	String[] PunctCharsList = new String[]{
		"'","\\|","\\/",
		"\u2026", // Ellipses
		"\u201c", // open quote
		"\u201d", // close quote
		"\"",".","?","!",",",":",";","&","*"
	};
	private String Punct;
	private String charPuncs;
	
	public PunctuationPatternProvider() {
		String [] allpuncs = new String[PunctCharsList.length];
		this.charPuncs = "[";
		int i = 0;
		for (String punc : PunctCharsList) {
			allpuncs[i++] = String.format("[%s]+",punc);
			charPuncs += punc;
		}
		charPuncs+="]";
		this.Punct = String.format("%s", RegexUtil.regex_or(allpuncs));
	}
	
	@Override
	public String patternString() {
		return charPuncs + "+";
	}
	
	public String charPattern(){
		return this.charPuncs;
	}
	
	public List<String> notMinus(String toIgnore){
		List<String> allnotpuncs = new ArrayList<String>();
		for (String punc : PunctCharsList) {
			if(toIgnore.equals(punc)) continue;
			allnotpuncs.add(String.format("^%s",punc));
		}
		return allnotpuncs;
	}

	
}
