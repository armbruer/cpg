/*
 * Copyright (c) 2020, Fraunhofer AISEC. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *                    $$$$$$\  $$$$$$$\   $$$$$$\
 *                   $$  __$$\ $$  __$$\ $$  __$$\
 *                   $$ /  \__|$$ |  $$ |$$ /  \__|
 *                   $$ |      $$$$$$$  |$$ |$$$$\
 *                   $$ |      $$  ____/ $$ |\_$$ |
 *                   $$ |  $$\ $$ |      $$ |  $$ |
 *                   \$$$$$   |$$ |      \$$$$$   |
 *                    \______/ \__|       \______/
 *
 */

package de.fraunhofer.aisec.cpg.graph.declarations;

import de.fraunhofer.aisec.cpg.graph.DeclarationHolder;
import de.fraunhofer.aisec.cpg.graph.Node;
import de.fraunhofer.aisec.cpg.graph.SubGraph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/** The top most declaration, representing a translation unit, for example a file. */
public class TranslationUnitDeclaration extends Declaration implements DeclarationHolder {

  /** A list of declarations within this unit. */
  @SubGraph("AST")
  @NonNull
  private List<Declaration> declarations = new ArrayList<>();

  /** A list of includes within this unit. */
  @SubGraph("AST")
  @NonNull
  private List<Declaration> includes = new ArrayList<>();

  /** A list of namespaces within this unit. */
  @SubGraph("AST")
  @NonNull
  private List<Declaration> namespaces = new ArrayList<>();

  /**
   * Returns the i-th declaration as a specific class, if it can be cast
   *
   * @param i the index
   * @param clazz the class
   * @param <T> the type of the class
   * @return the declaration or null, if it the declaration can not be cast to the class
   */
  @Nullable
  public <T extends Declaration> T getDeclarationAs(int i, Class<T> clazz) {
    Declaration declaration = this.declarations.get(i);

    if (declaration == null) {
      return null;
    }

    return declaration.getClass().isAssignableFrom(clazz)
        ? clazz.cast(this.declarations.get(i))
        : null;
  }

  /**
   * Returns a non-null, possibly empty {@code Set} of the declaration of a specified type and
   * clazz.
   *
   * <p>The set may contain more than one element if a declaration exists in the {@link
   * TranslationUnitDeclaration} itself and in an included header file.
   *
   * @param name the name to search for
   * @param clazz the declaration class, such as {@link FunctionDeclaration}.
   * @param <T> the type of the declaration
   * @return a {@code Set} containing the declarations, if any.
   */
  @NonNull
  public <T extends Declaration> Set<T> getDeclarationsByName(
      @NonNull String name, @NonNull Class<T> clazz) {
    return this.declarations.stream()
        .filter(declaration -> clazz.isAssignableFrom(declaration.getClass()))
        .map(clazz::cast)
        .filter(declaration -> Objects.equals(declaration.getName(), name))
        .collect(Collectors.toSet());
  }

  @NonNull
  public List<Declaration> getDeclarations() {
    return declarations;
  }

  @NonNull
  public List<Declaration> getIncludes() {
    return Collections.unmodifiableList(includes);
  }

  @NonNull
  public List<Declaration> getNamespaces() {
    return Collections.unmodifiableList(namespaces);
  }

  public void addDeclaration(@NonNull Declaration declaration) {
    if (declaration instanceof IncludeDeclaration) {
      includes.add(declaration);
    } else if (declaration instanceof NamespaceDeclaration) {
      namespaces.add(declaration);
    }

    addIfNotContains(declarations, declaration);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, Node.TO_STRING_STYLE)
        .append("declarations", declarations)
        .append("includes", includes)
        .append("namespaces", namespaces)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TranslationUnitDeclaration)) {
      return false;
    }
    TranslationUnitDeclaration that = (TranslationUnitDeclaration) o;
    return super.equals(that)
        && Objects.equals(declarations, that.declarations)
        && Objects.equals(includes, that.includes)
        && Objects.equals(namespaces, that.namespaces);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
