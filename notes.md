# Introduction
## What is ASM used for?
### Program analysis
* Syntaxic parsing
* Semantic analysis
* Find potentials bugs in applications
* Detect unused code
* Reverse engineer code

### Program generation
Used in compilers, including *traditional* compilers, Just in Time compilers, and stub or skeleton compilers used for distributed programming.

### Program tranformation
* Optimize programs
* Obfuscate programs
* Insert debugging or perf monitoring code into applications
* Used in AOP

## Why ASM?
* Simple, easy, well designed and modular API
* Well documented
* Supports Java 7
* Small, fast, robust
* Open Source

## Two kind of APIs
### Event based model
### Object based model

### Comparison of the two APIs
* The event based one is faster and requires less memory than the object based API.
* But it's harder to use than the object based API: one element of the class is
available at any given time

## Organization of the library
### org.objectweb.asm and org.objectweb.asm.signature
Deﬁne the event based API and provide the class parser and writer components.  
Archive: **asm.jar**.

### org.objectweb.asm.util
Provides various tools based on the core API that can be used during the development
and debuging of ASM applications.  
Archive: **asm-util.jar**.

### org.objectweb.asm.commons
Provides several useful predeﬁned class transformers, mostly based on the core API.  
Archive: **asm-commons.jar**.

### org.objectweb.asm.tree
Deﬁnes the object based API, and provides tools to convert between the event based
and the object based representations.  
Archive: **asm-tree.jar**.

### org.objectweb.asm.tree.analysis
Provides a class analysis framework and several predeﬁned class analyzers, based on
the tree API.  
Archive: **asm-analysis.jar**.

# Classes
## Structure
### A compiled class contains
* A section describing the modiﬁers (such as public or private), the name, the super class, the interfaces and the annotations of the class.
* One section per ﬁeld declared in this class. Each section describes the modiﬁers, the name, the type and the annotations of a ﬁeld.
* One section per method and constructor declared in this class.
Each section describes the modiﬁers, the name, the return and parameter types, and the annotations of a method.
It also contains the compiled code of the method, in the form of a sequence of Java bytecode instructions.

### Differences between source and compiled classes
* A compiled class describes one class only, while a source class can contain several classes.
A main class ﬁle contains *references* to its inner
classes, and inner classes deﬁned inside methods contain a *reference* to their enclosing method.
* A compiled class does not contain a *package* and *import* section, so all type names must be fully qualified.

### Internal names, type and method descriptors
#### Internal names
A type is usually a class or an interface.  
The internal name of theses types are its FQN where dots are replaces by slahshes.  
Example: `java/lang/String`.

#### Type descriptors
They represent types that aren't a class or an interface; a field type for example.  
The descriptor of a class type is constitued of:

* L 
* the internal name of this class
* semicolon

**Examples:**

* `Ljava/lang/String;`  
* For an array type: a square bracket followed by the descriptor of the array element type. `[I` for an `int`s array.

#### Method descriptors
A list of type descriptors that describe the parameter types and the return type of a method. 

**Example:**

* `void m(int i, float f)` -> `(IF)V`
* `int m(Object o)` -> `(Ljava/lang/Object;)I`
* `int[] m(int i, String s)` -> `(ILjava/lang/String;)[I`
* `Object m(int[] i)` -> `([I)Ljava/lang/Object;`