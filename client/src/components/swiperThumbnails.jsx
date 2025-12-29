"use client";
import React, { useState } from "react";

import { Swiper, SwiperSlide } from "swiper/react";
import { FreeMode, Navigation, Thumbs } from "swiper/modules";

import "swiper/css";
import "swiper/css/free-mode";
import "swiper/css/navigation";
import "swiper/css/thumbs";
import Image from "next/image";

export default function SwiperThumbnails({ images = [], size = 300 }) {
    const [thumbsSwiper, setThumbsSwiper] = useState(null);
    const [current, setCurrent] = useState(0);

    return (
        <figure
            className="flex items-center justify-center gap-5 w-full"
            style={{ maxWidth: `${size}px`, height: `${size - 120}px` }}
        >
            <div className="w-fit relative">
                <div className="badge badge-sm absolute top-2 right-2 z-10">
                    {current + 1 || 1} / {images.length}
                </div>

                <Swiper
                    style={{
                        "--swiper-navigation-color": "#4e99d3",
                        "--swiper-pagination-color": "#4e99d3",
                        width: `${size - 120}px`,
                    }}
                    spaceBetween={10}
                    navigation={true}
                    thumbs={{ swiper: thumbsSwiper }}
                    modules={[FreeMode, Navigation, Thumbs]}
                    onSlideChange={(swiper) => setCurrent(swiper.activeIndex)}
                    className="mySwiper2 bg-black/20 rounded"
                >
                    {images.map((image, index) => (
                        <SwiperSlide
                            key={image.id}
                            className="flex items-center justify-center h-[auto_!important]"
                        >
                            <img
                                src={image.url}
                                alt={image.alt}
                                style={{
                                    viewTransitionName: `product-${image.id}`,
                                }}
                                className="w-full h-full object-contain"
                            />
                        </SwiperSlide>
                    ))}
                </Swiper>
            </div>
            <div className="w-[100px] hidden md:block h-full">
                <Swiper
                    onSwiper={setThumbsSwiper}
                    spaceBetween={10}
                    slidesPerView={3}
                    watchSlidesProgress={true}
                    modules={[FreeMode, Navigation, Thumbs]}
                    className="mySwiper w-full h-full"
                    style={{ height: `${size - 120}px` }}
                    direction="vertical"
                >
                    {images.map((image, index) => (
                        <SwiperSlide
                            key={image.id}
                            className="aspect-square w-[100%_!important] bg-black/20 rounded overflow-hidden h-[auto_!important]"
                        >
                            <div className="w-full h-full flex items-center justify-center">
                                <Image
                                    src={image.url}
                                    alt={image.alt}
                                    width={100}
                                    height={100}
                                    style={{
                                        opacity: current === index ? 1 : 0.5,
                                        objectFit: "contain",
                                        scale: current === index ? 0.9 : 0.8,
                                    }}
                                />
                            </div>
                        </SwiperSlide>
                    ))}
                </Swiper>
            </div>
        </figure>
    );
}
