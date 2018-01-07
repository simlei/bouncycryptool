object NecessaryDummy {
  override def toString: String = "this object is necessary for the target platform packing to work since the assembly plugin " +
    "doesn't support empty class directories."
}
