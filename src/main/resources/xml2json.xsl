<?xml version="1.0" encoding="UTF-8" ?>
<!--
  ~ Jinx is Copyright 2010-2014 by Jeremy Brooks and Contributors
  ~
  ~ Jinx is free software: you can redistribute it and/or modify
  ~ it under the terms of the GNU General Public License as published by
  ~ the Free Software Foundation, either version 3 of the License, or
  ~ (at your option) any later version.
  ~
  ~ Jinx is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~ GNU General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public License
  ~ along with Jinx.  If not, see <http://www.gnu.org/licenses/>.
  ~
  ~ This XSLT was written by Bojan Bjelić - http://www.bjelic.net/2012/08/01/coding/convert-xml-to-json-using-xslt/
  ~ and is used under terms of the Creative Commons by-sa 3.0 license: http://creativecommons.org/licenses/by-sa/3.0/
  -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text" encoding="utf-8"/>

    <xsl:template match="/*[node()]">
        <xsl:text>{</xsl:text>
        <xsl:apply-templates select="." mode="detect" />
        <xsl:text>}</xsl:text>
    </xsl:template>

    <xsl:template match="*" mode="detect">
        <xsl:choose>
            <xsl:when test="name(preceding-sibling::*[1]) = name(current()) and name(following-sibling::*[1]) != name(current())">
                <xsl:apply-templates select="." mode="obj-content" />
                <xsl:text>]</xsl:text>
                <xsl:if test="count(following-sibling::*[name() != name(current())]) &gt; 0">, </xsl:if>
            </xsl:when>
            <xsl:when test="name(preceding-sibling::*[1]) = name(current())">
                <xsl:apply-templates select="." mode="obj-content" />
                <xsl:if test="name(following-sibling::*) = name(current())">, </xsl:if>
            </xsl:when>
            <xsl:when test="following-sibling::*[1][name() = name(current())]">
                <xsl:text>"</xsl:text><xsl:value-of select="name()"/><xsl:text>" : [</xsl:text>
                <xsl:apply-templates select="." mode="obj-content" /><xsl:text>, </xsl:text>
            </xsl:when>
            <xsl:when test="count(./child::*) > 0 or count(@*) > 0">
                <xsl:text>"</xsl:text><xsl:value-of select="name()"/>" : <xsl:apply-templates select="." mode="obj-content" />
                <xsl:if test="count(following-sibling::*) &gt; 0">, </xsl:if>
            </xsl:when>
            <xsl:when test="count(./child::*) = 0">
                <xsl:text>"</xsl:text><xsl:value-of select="name()"/>" : "<xsl:apply-templates select="."/><xsl:text>"</xsl:text>
                <xsl:if test="count(following-sibling::*) &gt; 0">, </xsl:if>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="*" mode="obj-content">
        <xsl:text>{</xsl:text>
        <xsl:apply-templates select="@*" mode="attr" />
        <xsl:if test="count(@*) &gt; 0 and (count(child::*) &gt; 0 or text())">, </xsl:if>
        <xsl:apply-templates select="./*" mode="detect" />
        <xsl:if test="count(child::*) = 0 and text() and not(@*)">
            <xsl:text>"</xsl:text><xsl:value-of select="name()"/>" : "<xsl:value-of select="text()"/><xsl:text>"</xsl:text>
        </xsl:if>
        <xsl:if test="count(child::*) = 0 and text() and @*">
            <xsl:text>"text" : "</xsl:text><xsl:value-of select="text()"/><xsl:text>"</xsl:text>
        </xsl:if>
        <xsl:text>}</xsl:text>
        <xsl:if test="position() &lt; last()">, </xsl:if>
    </xsl:template>

    <xsl:template match="@*" mode="attr">
        <xsl:text>"</xsl:text><xsl:value-of select="name()"/>" : "<xsl:value-of select="."/><xsl:text>"</xsl:text>
        <xsl:if test="position() &lt; last()">,</xsl:if>
    </xsl:template>

    <xsl:template match="node/@TEXT | text()" name="removeBreaks">
        <xsl:param name="pText" select="normalize-space(.)"/>
        <xsl:choose>
            <xsl:when test="not(contains($pText, '&#xA;'))"><xsl:copy-of select="$pText"/></xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="concat(substring-before($pText, '&#xD;&#xA;'), ' ')"/>
                <xsl:call-template name="removeBreaks">
                    <xsl:with-param name="pText" select="substring-after($pText, '&#xD;&#xA;')"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>