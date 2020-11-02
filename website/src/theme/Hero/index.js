import React from "react";
import clsx from "clsx";
import Link from "@docusaurus/Link";
import useBaseUrl from "@docusaurus/useBaseUrl";
import useDocusaurusContext from "@docusaurus/useDocusaurusContext";

import styles from "./styles.module.scss";

function Hero() {
    const context = useDocusaurusContext();
    const { siteConfig = {} } = context;

    return (
        <header id="hero" className={clsx("hero", styles.hero)}>
            <div className="container">
                <img
                    src={useBaseUrl(`img/logo.svg`)}
                    alt="Logo"
                    className={styles.logo}
                />
                <h1 className="hero__title">{siteConfig.title}</h1>
                <p className={clsx("hero__subtitle", styles.subtitle)}>
                    {siteConfig.tagline}
                </p>

                <div className={styles.buttons}>
                  <Link
                      className={clsx(
                          "button button--primary button--lg",
                          styles.getStarted
                      )}
                      to="https://plugins.jetbrains.com/plugin/9440-reasonml"
                  >
                      Download
                  </Link>
                  <Link
                    className={clsx(
                      "button button--secondary button--lg",
                      styles.getStarted
                    )}
                    to={useBaseUrl("docs/")}
                  >
                    Get Started
                  </Link>
                </div>
            </div>
        </header>
    );
}

export default Hero;
