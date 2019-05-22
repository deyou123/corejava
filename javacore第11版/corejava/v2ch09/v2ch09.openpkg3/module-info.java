@SuppressWarnings("module")
module v2ch09.openpkg3
{
   requires com.google.gson;
   opens com.horstmann.places to com.google.gson;
}
