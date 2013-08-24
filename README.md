Sloot Editor
====================

Sloot Editor is a [Box2D](http://box2d.org/) editor with support for entities, screens and scripting.

See [sloot-api](https://github.com/hemantasapkota/sloot-api), if you're interested in integrating sloot projects
in your custom app.

Features
===================
1. Extract from spritesheet, convert to entities
2. Define animation frames for entities
3. Define collision shapes (circle and box)
4. Support for [Physics Editor](http://www.aurelienribon.com/blog/projects/physics-body-editor/) generated fixtures 
5. Layout screen with entities as well as box2d entities shapes (circle, box)
6. Render screen
7. Attach joints to entities/non-entities (Distance, Revolute, Prismatic, Pulley, Friction, Weld)
8. Lua scripting support (*experimental)
9. Export entities/screen to JSON

Technology
====================
1. [Eclipse RCP](http://www.eclipse.org/home/categories/rcp.php) for platform management
2. [Eclipse GEF](https://github.com/eclipse/gef) for screen editor
3. [Libgdx](https://github.com/libgdx/libgdx) for rendering
4. [Google Protobuf](https://code.google.com/p/protobuf/) for defining format for entities and screens
5. [LuaJ](http://luaj.org/luaj/README.html) for executing lua scripts from Java

Limitations
====================
1. Cannot rotate entities/non-entities in editor. [GEF does not support rotating edit parts]
2. Joint support is basic. (No ragdolls yet...)

Build/Install/Run
====================

See wiki page [How to Build](https://github.com/hemantasapkota/Laexian_Box2D_Editor/wiki/How-to-Build)

Screenshots
===========
Editor Preview

![Preview Laexian Box2D Editor](https://raw.github.com/hemantasapkota/Laexian_Box2D_Editor/master/screenshots/EdPreview.png)

Render Screen

![Render Screen Laexian Box2D Editor](https://raw.github.com/hemantasapkota/Laexian_Box2D_Editor/master/screenshots/EdRender.png)

Outline View

![Screen Outline Laexian Box2D Editor](https://raw.github.com/hemantasapkota/Laexian_Box2D_Editor/master/screenshots/EdOutline.png)

Lua Scripting

![Screen Outline Laexian Box2D Editor](https://raw.github.com/hemantasapkota/Laexian_Box2D_Editor/master/screenshots/EdLuaScript.png)
