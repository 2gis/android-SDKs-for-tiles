# Android Demo project 

The map is automatically centered according to points, but if points are not added, specify the starting position.

```
private static String START_GEO = "25.220317,55.232048,13";

```


Addition points through showMarkers function.

```
List<Marker> markers = new ArrayList<>();
markers.add(new Marker("70000001006275165", 55.288396, 25.263831));
markers.add(new Marker("70000001006186603", 55.285606, 25.260066));
markers.add(new Marker("70000001007008967", 55.318308, 25.24457));
showMarkers(markers);

```


Point pictures can be replaced.

```
./TileMap/app/src/assets/images/point.svg
./TileMap/app/src/assets/images/point_active.svg

```


Processing of a click on a point occurs in touchMarker function.

```
./TileMap/app/src/assets/images/point.svg
./TileMap/app/src/assets/images/point_active.svg

```


You can also add a custom image of another object.

```
private String prepareMarkerMessage(List<Marker> markers) {
    return getMarkersArray(markers) + "var lat;var lon;setMarkersAndAvatar(markers, 25.248772, 55.36103, 'https://cdn-new.flamp.us/default-avatar-m_100_100.png');";
}

```
