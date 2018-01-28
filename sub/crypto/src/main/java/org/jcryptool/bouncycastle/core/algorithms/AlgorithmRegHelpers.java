package org.jcryptool.bouncycastle.core.algorithms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.engines.AESWrapEngine;
import org.jcryptool.bouncycastle.core.Activator;
import org.jcryptool.bouncycastle.core.operation.blocksym.BlockSymOperation;
import org.jcryptool.bouncycastle.core.operation.blocksym.BlockSymSpec;
import org.jcryptool.bouncycastle.core.operation.digest.DigestSpec;
import org.jcryptool.bouncycastle.core.util.FileUtils;

//import com.thoughtworks.xstream.XStream;
//import com.thoughtworks.xstream.io.xml.DomDriver;

public class AlgorithmRegHelpers {

	public static List<BlockSymSpec> makeDefaultBlocksymAlgorithms() {
		LinkedList<BlockSymSpec> result = new LinkedList<BlockSymSpec>();
		for(BsSpecs spec: BsSpecs.values()) {
			BlockSymSpec specO = spec.spec;
			result.add(specO);
		}
		return result;
	}

	
	public static List<DigestSpec> makeDefaultDigestAlgorithms() {
		LinkedList<DigestSpec> result = new LinkedList<DigestSpec>();
		for(DigestSpecs spec: DigestSpecs.values()) {
			DigestSpec specO = spec.spec;
			result.add(specO);
		}
		return result;
	}
}
