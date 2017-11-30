package org.superbiz.rx;

class Dish {
    private final byte[] oneKb = new byte[1_024];
    private final int id;

    Dish(int id) {
        this.id = id;
        System.out.println("Created: " + id);
    }

    public String toString() {
        return String.valueOf(id);
    }
}