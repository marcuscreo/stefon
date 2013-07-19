# stefon

A composable blogging engine in Clojure. This project will try to approach the feature set of [Wordpress](http://codex.wordpress.org/WordPress_Features).
 * Core Data Model
   * posts (txt, rtf)
   * assets (images, audio, videos, documents)
   * tags / categories for content
 * Server component;
   * create post / asset / tag
   * retrieve post / asset
   * update post / asset / tag
   * delete post / asset / tag
   * list posts / assets / tags
   * find posts / assets / tags
 * Workflow component;
   * preview
   * collaboration
   * editor review
   * versioning
 * Plug-in support
 * Database component;
   * adapters for [Datomic](http://www.datomic.com), SQL([Postgres](http://www.postgresql.org), etc), NoSQL ([Mongo](http://www.mongodb.org), etc), cloud storage (AWS [SimpleDB](http://aws.amazon.com/simpledb), [S3](http://aws.amazon.com/s3))
 * Web UI component;
   * wyswyg editor, themes
   * embeddable in Compojure or Pedestal
 * Authentication & Authorization; OpenID
 * Spam Detection
 * Commenting component; default or an external comments service, like disqus or discourse
 * Administration Console
 * Import / Export
 * Multi-lang / Internationalization


## Usage

TBD

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
