package org.jcryptool.bouncycastle.core.algorithms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import org.jcryptool.bouncycastle.core.Activator;
import org.jcryptool.bouncycastle.core.operation.blocksym.BlockSymConfig;
import org.jcryptool.bouncycastle.core.operation.blocksym.BlockSymDefaults;
import org.jcryptool.bouncycastle.core.operation.blocksym.BlockSymOperation;
import org.jcryptool.bouncycastle.core.operation.blocksym.BlockSymSpec;
import org.jcryptool.bouncycastle.core.operation.digest.DigestOperation;
import org.jcryptool.bouncycastle.core.operation.digest.DigestSpec;
//import org.jcryptool.core.logging.utils.LogUtil;

//import com.thoughtworks.xstream.XStream;
//import com.thoughtworks.xstream.io.xml.DomDriver;

public class AlgorithmReg extends Observable {

	private static final String DEFAULT_DB_PATH = "algorithms.bc.xml";
	private static AlgorithmReg defaultInstance = null;
	
	private LinkedList<BlockSymSpec> specs_blocksym;
	private LinkedList<BlockSymOperation> favorites_blocksym;
	private LinkedList<DigestSpec> specs_digests;

	private AlgorithmReg() {
	}
	
//	private static AlgorithmReg loadRegFromFile(FileReader reader) {
//		XStream xstream = new XStream(new DomDriver());
//		xstream.setClassLoader(Activator.getDefault().getClass().getClassLoader());
//
////				xstream.alias("alphabets", Vector.class);
////				xstream.alias("alphabet", Alphabet.class);
//
//		ARegPersistence persistenceObject = (ARegPersistence) xstream.fromXML(reader);
//		return persistenceObject.makeReg();
//	}
//
//	private void saveRegAsFile(AlgorithmReg reg, Writer w) {
//		ARegPersistence persistenceObject = new ARegPersistence(specs_blocksym, favorites_blocksym, specs_digests);
//
//		XStream xstream = new XStream(new DomDriver());
//
////				xstream.alias("alphabets", Vector.class);
////				xstream.alias("alphabet", Alphabet.class);
//		xstream.toXML(persistenceObject, w);
//	}
//
	protected AlgorithmReg(List<? extends BlockSymSpec> specs_blocksym, List<? extends DigestSpec> digestSpecs, List<? extends BlockSymOperation> favorites_blocksym) {
		if(specs_blocksym!=null) {
			this.specs_blocksym = new LinkedList<BlockSymSpec>(specs_blocksym);
		} else {
			this.specs_blocksym = new LinkedList<BlockSymSpec>();
		}

		if(digestSpecs != null) {
			this.specs_digests = new LinkedList<DigestSpec>(digestSpecs);
		} else {
			this.specs_digests = new LinkedList<DigestSpec>();
		}

		if(favorites_blocksym != null) {
			this.favorites_blocksym = new LinkedList<BlockSymOperation>(favorites_blocksym);
		} else {
			this.favorites_blocksym = new LinkedList<BlockSymOperation>();
		}
	}
//
//	private static AlgorithmReg loadFile(File regFile) {
//		if(!regFile.exists()) throw new IllegalArgumentException("no actual Algorithm reg file: " + regFile.toString());
//		if(!regFile.isFile()) throw new IllegalArgumentException("no actual Algorithm reg file: " + regFile.toString());
//
//		try {
//			FileReader r = new FileReader(regFile);
//			AlgorithmReg reg = loadRegFromFile(r);
//			r.close();
//			return reg;
//
//		} catch (FileNotFoundException e) {
//			throw new RuntimeException(e);
//			//TODO: handle better
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//			// TODO Auto-generated catch block
//		}
//	}
//
//	public void saveToFile() {
//		saveToFile(DEFAULT_DB_PATH);
//	}
//
//	private void saveToFile(String defaultDbPath) {
//		File f = new File(defaultDbPath);
//		FileWriter w;
//		try {
//			w = new FileWriter(f);
//			saveRegAsFile(this, w);
//			w.close();
//		} catch (IOException e) {
//			LogUtil.logError(e);
//			e.printStackTrace();//TODO: handle better
//		}
//	}

	private static AlgorithmReg generateDefaultReg() {
		List<BlockSymSpec> defaultBSSpecs = AlgorithmRegHelpers.makeDefaultBlocksymAlgorithms();
		List<DigestSpec> defaultDigestSpecs = AlgorithmRegHelpers.makeDefaultDigestAlgorithms();
		
		return new AlgorithmReg(defaultBSSpecs, defaultDigestSpecs, null);
	}
	
	public static AlgorithmReg getInstanceDefault() {
		if(defaultInstance != null) return defaultInstance;
		
		File file = new File(DEFAULT_DB_PATH);
		try {
//			AlgorithmReg.defaultInstance = loadFile(file);
			throw new RuntimeException("not supported to load from file here");
		} catch (Exception e) {
			AlgorithmReg.defaultInstance = generateDefaultReg();
		}
		
		return AlgorithmReg.getInstanceDefault(); 
	}
	
	public void addBSFavorite(BlockSymOperation op) {
		
		LinkedList<BlockSymOperation> removeList = new LinkedList<BlockSymOperation>();
		
		for(BlockSymOperation o: this.favorites_blocksym) {
			if(o.getName().equals(op.getName())) {
				removeList.add(o);
			}
		}
		this.favorites_blocksym.removeAll(removeList);
		
		this.favorites_blocksym.add(op);
		this.setChanged();
		this.notifyObservers();
	}

	public List<BlockSymSpec> getSpecs_blocksym() {
		return new LinkedList<BlockSymSpec>(this.specs_blocksym);
	}
	public List<DigestSpec> getSpecs_digest() {
		return new LinkedList<DigestSpec>(this.specs_digests);
	}
	
	public List<BlockSymOperation> getTemplatesBS() {
		List<BlockSymOperation> default_templates = new LinkedList<BlockSymOperation>();
		List<BlockSymSpec> specs_blocksym = getSpecs_blocksym();
		for (int i = 0; i < specs_blocksym.size(); i++) {
			BlockSymSpec spec = specs_blocksym.get(i);
			BlockSymConfig defaultCfg = BlockSymDefaults.makeDefaultCfg(spec);
			BlockSymOperation template = new BlockSymOperation(spec, defaultCfg, null);
			
			default_templates.add(template);
		}
		return default_templates;
	}
	public List<DigestOperation> getTemplatesDigests() {
		List<DigestOperation> default_templates = new LinkedList<DigestOperation>();
		List<DigestSpec> specs_digests = getSpecs_digest();
		for (int i = 0; i < specs_digests.size(); i++) {
			DigestSpec spec = specs_digests.get(i);
			DigestOperation template = new DigestOperation(spec);
			
			default_templates.add(template);
		}
		return default_templates;
	}
	public List<BlockSymOperation> getFavoritesBS() {
		return new LinkedList<BlockSymOperation>(this.favorites_blocksym);
	}
	
	
}
