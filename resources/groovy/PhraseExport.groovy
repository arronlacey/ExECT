new File(scriptParams.outputFile).withWriterAppend{ out ->
for (doc in docs) {
 AnnotationSet anns = doc.getAnnotations("Bio").get("Phrase3")
 for (ann in anns) {
  println(ann.getFeatures().get("text"))
 }
}
}
