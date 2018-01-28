package org.jcryptool.bouncycastle.core.operation.blocksym;

import java.lang.reflect.Constructor;
import java.nio.charset.Charset;

import javax.print.attribute.standard.MediaSize.Engineering;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Hex;
import org.jcryptool.bouncycastle.core.algorithms.BsSpecs;
import org.jcryptool.bouncycastle.core.operation.BCOperation;
import org.jcryptool.bouncycastle.core.operation.Input;
import org.jcryptool.bouncycastle.core.operation.Output;
import org.jcryptool.bouncycastle.core.operation.data.UTF8StringInput;

/**
 * This class represents the pre-execution state of a general 
 * block-symmetric bouncy castle algorithm execution.
 * 
 * Within such an execution the following parameters need to be determined:
 *  - 
 *  
 * @author Simon
 *
 */
public class BlockSymOperation extends BCOperation {
	
	public BlockSymSpec spec;
	public BlockSymConfig cfg;
	public BlockSymOpParameters key;
	
	private BlockSymOperation() {
		this(null, null, null);
	}
	
	public BlockSymOperation(BlockSymSpec spec, BlockSymConfig cfg,
			BlockSymOpParameters key) {
		//TODO: usual argschecks
		super(spec==null?null: spec.id_name);
		this.spec = spec;
		this.cfg = cfg;
		this.key = key;
	}
	
	
	private BlockCipher constructEngine() {
		if(this.spec.engine == null) throw new RuntimeException("block cipher engine class == null");
			
		Class<? extends BlockCipher> cl = this.spec.engine;
		
		Constructor[] cs = cl.getConstructors();
		
		for(Constructor constructor: cs) { // try blocksize constructors first!
			if(constructor.getParameterTypes().length == 1 && constructor.getParameterTypes()[0].equals(int.class)) {
				try {
					return (BlockCipher) constructor.newInstance(new Object[]{cfg.blocksize_bits});
				} catch (Exception e) {
					// TODO this should never happen; log this...
					throw new RuntimeException(e);
				}
			}
		}
		for(Constructor constructor: cs) {
			if(constructor.getParameterTypes().length == 0) {
				try {
					return (BlockCipher) constructor.newInstance(new Object[]{});
				} catch (Exception e) {
					// TODO this should never happen; log this...
					throw new RuntimeException(e);
				}
			}
		}
		
		//TODO: better handling
		throw new RuntimeException("No suitable engine constructor found for engine class " + cl.toString() + ", spec id = " + this.spec.id_name);
	}
	
	private PaddedBufferedBlockCipher wrapEngine(BlockCipher baseEngine, BlockSymSpec spec,
			BlockSymConfig cfg) {
		
		BlockCipherPadding padding = cfg.padding.getPadding();
		BlockCipher moded = cfg.mode.applyMode(baseEngine, cfg.blocksize_bits);
		
		return new PaddedBufferedBlockCipher(moded, padding);
	}

	public byte[] executeOperation(byte[] inData) throws CryptoException {
		//TODO: instance state check
		
		
		BlockCipher baseEngine = constructEngine();
		PaddedBufferedBlockCipher opEngine = wrapEngine(baseEngine, spec, cfg);
		
		CipherParameters bcBaseKey = new KeyParameter(this.key.data);
		ParametersWithIV bcParam = new ParametersWithIV(bcBaseKey, this.cfg.iv);
		
        opEngine.init(this.key.encrypt, bcParam);

        int chunksize = 1; //TODO: better
        byte[][] chunks = makeChunks(inData, chunksize);
        byte[] outData = new byte[opEngine.getOutputSize(inData.length)+2*opEngine.getBlockSize()];
        
        int lenSum = 0;
        for (int i = 0; i < chunks.length; i++) {
			byte[] chk = chunks[i];
			int len1 = opEngine.processBytes(chk, 0, chk.length, outData, lenSum);
        	lenSum += len1;
		}
        
        try{
        	int lenF = opEngine.doFinal(outData, lenSum);
        	lenSum += lenF;
        } catch (CryptoException e) {
			if(key.encrypt) {
				throw e;
			} else {
				System.out.println("doFinal exception: " + e.getMessage());
			}
		}
        
        byte[] result = new byte[lenSum];
        System.arraycopy(outData, 0, result, 0, lenSum);
        
        return result;
	}


	private byte[][] makeChunks(byte[] inData, int chunksize) {
		byte[][] chks = new byte[(int) Math.ceil((double)(inData.length)/chunksize)][chunksize];
		for (int i = 0; i < chks.length; i++) {
			int startIdx = i*chunksize;
			//TODO: better
			for(int k=0; k<Math.min(chunksize, inData.length-startIdx); k++) {
//				System.arraycopy(inData, startIdx, chks[i], 0, chunksize);
				chks[i][k] = inData[startIdx+k];
			}
		}
		return chks;
	}
	
}
