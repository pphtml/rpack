package org.superbiz.rx;

import rx.Observable;

import java.time.LocalDate;

public class CartesianProduct {
    public static void main(String[] args) {
        Observable<LocalDate> nextTenDays =
                Observable
                        .range(1, 10)
                        .map(i -> LocalDate.now().plusDays(i));
        Observable<Vacation> possibleVacations = Observable
                .just(City.Warsaw, City.London, City.Paris)
                .flatMap(city -> nextTenDays.map(date -> new Vacation(city, date))
                        .flatMap(vacation ->
                                Observable.zip(
                                        vacation.weather().filter(Weather::isSunny),
                                        vacation.cheapFlightFrom(City.NewYork),
                                        vacation.cheapHotel(),
                                        (w, f, h) -> vacation
                                )));
    }

    private static class Vacation {
        private final City where;
        private final LocalDate when;
        Vacation(City where, LocalDate when) {
            this.where = where;
            this.when = when;
        }
        public Observable<Weather> weather() {
            return null;
        }
        public Observable<Flight> cheapFlightFrom(City from) {
            return null;
        }
        public Observable<Hotel> cheapHotel() {
            return null;
        }
    }

    private enum  City {
        London, Paris, NewYork, Warsaw
    }

    private static class Weather {
        private boolean sunny;

        public static boolean isSunny(Weather weather) {
            return weather.sunny;
        }
    }

    private static class Flight {
    }

    private static class Hotel {
    }
}
