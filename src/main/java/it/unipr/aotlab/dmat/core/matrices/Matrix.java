/*
 * Copyright (c) 2011. Enrico Franchi <efranchi@ce.unipr.it>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.initializers.Initializer;

/**
 * User: enrico
 * Package: it.unipr.aotlab.dmat.core.matrices
 * Date: 10/17/11
 * Time: 2:51 PM
 */
public interface Matrix {
    /* TODO: define good format to specify initialization of matrix
     * we probably need something generic like:
     * 1. specifiy values directly
     * 2. specify an URI from which to get the values (URI means that
     *    it can be either a local file for the specific node where the
     *    nodes should be stored or other stuff
     * 3. do we need something clever to manage the fact that some nodes
     *    may want to use different URIs to get their values?
     * 4. what about the formats in which we store the values in the files?
     */

    /**
     * Sets the initializer to the specified initializer.
     * @param init
     * @return setInitializer always returns this
     */
    Matrix setInitializer(Initializer init);

    /**
     * Initializes the matrix with the specified initializer.
     *
     * TODO decide if It is an error to call this method more than once
     * @return this
     */
    Matrix initialize();
}
