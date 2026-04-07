<h1 align="center">
  <img src="https://readme-typing-svg.demolab.com?font=JetBrains+Mono&weight=600&size=28&duration=3000&pause=1000&color=B8D8B0&center=true&vCenter=true&random=false&width=500&lines=%C3%96mer+Ekmen;Data+Scientist;Software+Engineer;Mathematics+Graduate" alt="Typing SVG" />
</h1>

<p align="center">
  <a href="https://www.omerekmen.com"><img src="https://img.shields.io/badge/Portfolio-omerekmen.com-B8D8B0?style=for-the-badge&logo=safari&logoColor=white" alt="Portfolio" /></a>
  <a href="https://linkedin.com/in/omerekmenn"><img src="https://img.shields.io/badge/LinkedIn-omerekmenn-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white" alt="LinkedIn" /></a>
  <a href="mailto:omerekmenn@gmail.com"><img src="https://img.shields.io/badge/Email-omerekmenn-EA4335?style=for-the-badge&logo=gmail&logoColor=white" alt="Email" /></a>
</p>

---

# Turkcell GYGY 5.0 Java Practices

This repository includes Java exercises and mini-applications created during the **Turkcell GYGY 5.0 Java** training program.

## Workspace Layout

```text

TURKCELL GYGY/
  └─ turkcell-gygy-java/
    └─ banking-app/
```

## Banking App Summary

The banking app is a Maven-based Java 21 console application with:

- user login and registration by TCKN
- in-memory user and bank account repositories
- account detail viewing
- deposit, withdrawal, and transfer flows
- mock data loaded at startup for demo use

## Build And Run

From the app folder:

```bash
cd TURKCELL\ GYGY/turkcell-gygy-java/banking-app
mvn clean compile
java -cp target/classes com.banking.Main
```

The included [Makefile](TURKCELL%20GYGY/turkcell-gygy-java/banking-app/Makefile) also provides:

- `make compile`
- `make runclass`
- `make full`

## Notes

- The banking app stores data only in memory, so all state resets when the program exits.
- Passwords are stored in plain text in the current training implementation.
- The root of this workspace is mainly for coordination and reference across projects.
