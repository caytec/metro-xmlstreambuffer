/*
 * Copyright (c) 2005, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.stream.buffer;

import org.xml.sax.Attributes;

/**
 * Class for holding attributes.
 *
 * Since it implements {@link Attributes}, this class follows the SAX convention
 * of using "" instead of null.
 */
@SuppressWarnings({"PointlessArithmeticExpression"})
public final class AttributesHolder implements Attributes {
    private static final int DEFAULT_CAPACITY = 8;
    private static final int ITEM_SIZE = 1 << 3;

    private static final int PREFIX     = 0;
    private static final int URI        = 1;
    private static final int LOCAL_NAME = 2;
    private static final int QNAME      = 3;
    private static final int TYPE       = 4;
    private static final int VALUE      = 5;

    private int _attributeCount;

    private String[] _strings;

    public AttributesHolder() {
        _strings = new String[DEFAULT_CAPACITY * ITEM_SIZE];
    }

    public final int getLength() {
        return _attributeCount;
    }

    public final String getPrefix(int index) {
        return (index >= 0 && index < _attributeCount) ?
            _strings[(index << 3) + PREFIX] : null;
    }

    public final String getLocalName(int index) {
        return (index >= 0 && index < _attributeCount) ?
            _strings[(index << 3) + LOCAL_NAME] : null;
    }

    public final String getQName(int index) {
        return (index >= 0 && index < _attributeCount) ?
            _strings[(index << 3) + QNAME] : null;
    }

    public final String getType(int index) {
        return (index >= 0 && index < _attributeCount) ?
            _strings[(index << 3) + TYPE] : null;
    }

    public final String getURI(int index) {
        return (index >= 0 && index < _attributeCount) ?
            _strings[(index << 3) + URI] : null;
    }

    public final String getValue(int index) {
        return (index >= 0 && index < _attributeCount) ?
            _strings[(index << 3) + VALUE] : null;
    }

    public final int getIndex(String qName) {
        for (int i = 0; i < _attributeCount; i++) {
            if (qName.equals(_strings[(i << 3) + QNAME])) {
                return i;
            }
        }
        return -1;
    }

    public final String getType(String qName) {
        final int i = (getIndex(qName) << 3) + TYPE;
        return (i >= 0) ? _strings[i] : null;
    }

    public final String getValue(String qName) {
        final int i = (getIndex(qName) << 3) + VALUE;
        return (i >= 0) ? _strings[i] : null;
    }

    public final int getIndex(String uri, String localName) {
        for (int i = 0; i < _attributeCount; i++) {
            if (localName.equals(_strings[(i << 3) + LOCAL_NAME]) &&
                uri.equals(_strings[(i << 3) + URI])) {
                return i;
            }
        }
        return -1;
    }

    public final String getType(String uri, String localName) {
        final int i = (getIndex(uri, localName) << 3) + TYPE;
        return (i >= 0) ? _strings[i] : null;
    }

    public final String getValue(String uri, String localName) {
        final int i = (getIndex(uri, localName) << 3) + VALUE;
        return (i >= 0) ? _strings[i] : null;
    }

    public final void clear() {
        if (_attributeCount > 0) {
            for (int i = 0; i < _attributeCount; i++) {
                _strings[(i << 3) + VALUE] = null;
            }
            _attributeCount = 0;
        }
    }


    /**
     * Add an attribute using a qualified name that contains the 
     * prefix and local name.
     *
     * @param uri
     *      This can be empty but not null, just like everywhere else in SAX.
     */
    public final void addAttributeWithQName(String uri, String localName, String qName, String type, String value) {
        final int i = _attributeCount << 3;
        if (i == _strings.length) {
            resize(i);
        }

        _strings[i + PREFIX] = null;
        _strings[i + URI] = uri;
        _strings[i + LOCAL_NAME] = localName;
        _strings[i + QNAME] = qName;
        _strings[i + TYPE] = type;
        _strings[i + VALUE] = value;

        _attributeCount++;
    }

    /**
     * Add an attribute using a prefix.
     *
     * @param prefix
     *      This can be empty but not null, just like everywhere else in SAX.
     * @param uri
     *      This can be empty but not null, just like everywhere else in SAX.
     */
    public final void addAttributeWithPrefix(String prefix, String uri, String localName, String type, String value) {
        final int i = _attributeCount << 3;
        if (i == _strings.length) {
            resize(i);
        }

        _strings[i + PREFIX] = prefix;
        _strings[i + URI] = uri;
        _strings[i + LOCAL_NAME] = localName;
        _strings[i + QNAME] = null;
        _strings[i + TYPE] = type;
        _strings[i + VALUE] = value;

        _attributeCount++;
    }

    private void resize(int length) {
        final int newLength = length * 2;
        final String[] strings = new String[newLength];
        System.arraycopy(_strings, 0, strings, 0, length);
        _strings = strings;
    }

}
