/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Bjorn Freeman-Benson - initial API and implementation
 *******************************************************************************/
// original package from IBM debugger exercises
// package org.eclipse.debug.examples.ui.pda.editor;
package luaeditorideplugin;

import java.util.Iterator;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;

/**
 * Returns hover for breakpoints.
 */
public class AnnotationHover implements IAnnotationHover {

	public String getHoverInfo(ISourceViewer sourceViewer, int lineNumber) {
		IAnnotationModel annotationModel = sourceViewer.getAnnotationModel();
		Iterator iterator = annotationModel.getAnnotationIterator();
		while (iterator.hasNext()) {
			Annotation annotation = (Annotation) iterator.next();
			Position position = annotationModel.getPosition(annotation);
			try {
				int lineOfAnnotation = sourceViewer.getDocument().getLineOfOffset(position.getOffset());
				if (lineNumber == lineOfAnnotation) {
					return annotation.getText();
				}
			} catch (BadLocationException e) {
			}
		}
		return null;
	}

}
