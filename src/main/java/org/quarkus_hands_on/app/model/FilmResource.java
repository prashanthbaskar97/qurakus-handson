package org.quarkus_hands_on.app.model;


import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.quarkus_hands_on.app.model.repository.FilmRepository;

import java.awt.*;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/")
public class FilmResource {

    @Inject
    FilmRepository filmRepository;

    @GET
    @Path("/helloWorld")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(){
        return "Hello World";
    }

    @GET
    @Path("/film/{filmId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getFilm(short filmId){
       Optional<Film> film= filmRepository.getFilm(filmId);
       return film.isPresent()?film.get().getTitle(): "No Film was found";

    }

    @GET
    @Path("/pagedFilms/{page}/{minLength}")
    @Produces(MediaType.TEXT_PLAIN)
public String paged(long page, short minLength){
    return filmRepository.paged(page , minLength)
            .map(f ->String.format("%s (%d min)",f.getTitle(),f.getLength()) )
            .collect(Collectors.joining("\n"));
    }


    @GET
    @Path("/actors/{startsWith}/{minLength}")
    @Produces(MediaType.TEXT_PLAIN)
    public String actors(String startsWith ,short minLength){

        return filmRepository.actors(startsWith ,minLength)
                .map(film -> String.format("%s (%d min): %s", film.getTitle(), film.getLength(), film.getActors().stream()
                        .map(a -> String.format ("%s %s", a.getFirstName(),a.getLastName()))
                        .collect(Collectors.joining(","))))

                .collect(Collectors.joining("\n"));
    }



    @GET
    @Path("/update/{minLength}/{rentalRate}")
    @Produces(MediaType.TEXT_PLAIN)
    public String update(short minLength, Float rentalRate){
        filmRepository.updateRentalRate(minLength,rentalRate);
        return filmRepository.getFilms(minLength)
                        .map(f ->String.format("%s (%d min) - $%f",f.getTitle(),f.getLength(),f.getRentalRate()) )
                        .collect(Collectors.joining("\n"));
    }

}
