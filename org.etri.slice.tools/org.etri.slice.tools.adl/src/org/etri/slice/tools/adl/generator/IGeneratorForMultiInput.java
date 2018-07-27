package org.etri.slice.tools.adl.generator;

import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;

public interface IGeneratorForMultiInput extends IGenerator {
    /**
     * @param input - the input for which to generate resources
     * @param fsa - file system access to be used to generate files
     */
    public void doGenerate(List<Resource> resources, IFileSystemAccess fsa);
}